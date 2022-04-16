/**
 * Copyright 2021 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bernardomg.darksouls.explorer.persistence.executor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Functions;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.cypherdsl.core.StatementBuilder.OngoingReadingAndReturn;
import org.neo4j.cypherdsl.core.SymbolicName;
import org.neo4j.cypherdsl.parser.CypherParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.support.PageableExecutionUtils;

public final class TextQueryExecutor implements QueryExecutor<String> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory
        .getLogger(TextQueryExecutor.class);

    private final Neo4jClient   client;

    public TextQueryExecutor(final Neo4jClient clnt) {
        super();

        client = Objects.requireNonNull(clnt);
    }

    @Override
    public final <T> Collection<T> fetch(final String query,
            final Function<Iterable<Map<String, Object>>, List<T>> mapper) {
        final Iterable<Map<String, Object>> read;

        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        read = client.query(query)
            .fetch()
            .all()
            .stream()
            .collect(Collectors.toList());
        return mapper.apply(read);
    }

    @Override
    public final <T> Collection<T> fetch(final String query,
            final Function<Iterable<Map<String, Object>>, List<T>> mapper,
            final Map<String, Object> parameters) {
        final Iterable<Map<String, Object>> read;

        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        read = client.query(query)
            .bindAll(parameters)
            .fetch()
            .all()
            .stream()
            .collect(Collectors.toList());
        return mapper.apply(read);
    }

    @Override
    public final <T> Page<T> fetch(final String query,
            final Function<Iterable<Map<String, Object>>, List<T>> mapper,
            final Map<String, Object> parameters, final Pageable page) {
        final List<T> data;
        final Collection<Map<String, Object>> read;
        final Statement baseStatement;
        final Optional<String> sort;
        String finalQuery;

        baseStatement = CypherParser.parseStatement(query);

        finalQuery = query;

        // Sort
        if (page.getSort()
            .isSorted()) {
            sort = page.getSort()
                .stream()
                .map(this::getOrder)
                .reduce((a, b) -> a + ", " + b);
            if (sort.isPresent()) {
                finalQuery += " ORDER BY " + sort.get();
            }
        }

        // Pagination
        if (page.isPaged()) {
            finalQuery += String.format(" SKIP %d",
                page.getPageNumber() * page.getPageSize());
            finalQuery += String.format(" LIMIT %d", page.getPageSize());
        }

        LOGGER.debug("Query: {}", finalQuery);

        // Data is fetched and mapped
        read = client.query(finalQuery)
            .bindAll(parameters)
            .fetch()
            .all();
        data = mapper.apply(read.stream()
            .collect(Collectors.toList()));

        return PageableExecutionUtils.getPage(data, page,
            () -> count(baseStatement));
    }

    @Override
    public final <T> Page<T> fetch(final String query,
            final Function<Iterable<Map<String, Object>>, List<T>> mapper,
            final Pageable page) {
        return fetch(query, mapper, Collections.emptyMap(), page);
    }

    @Override
    public final Collection<Map<String, Object>> fetch(final String query,
            final Map<String, Object> parameters) {
        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        return client.query(query)
            .bindAll(parameters)
            .fetch()
            .all();
    }

    @Override
    public final <T> Optional<T> fetchOne(final String query,
            final Function<Iterable<Map<String, Object>>, T> mapper) {
        final Iterable<Map<String, Object>> read;
        final T mapped;

        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        read = client.query(query)
            .fetch()
            .first()
            .stream()
            .collect(Collectors.toList());
        mapped = mapper.apply(read);
        return Optional.ofNullable(mapped);
    }

    @Override
    public final <T> Optional<T> fetchOne(final String query,
            final Function<Iterable<Map<String, Object>>, T> mapper,
            final Map<String, Object> parameters) {
        final Iterable<Map<String, Object>> read;
        final T mapped;

        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        read = client.query(query)
            .bindAll(parameters)
            .fetch()
            .first()
            .stream()
            .collect(Collectors.toList());
        mapped = mapper.apply(read);
        return Optional.ofNullable(mapped);
    }

    @Override
    public final Optional<Map<String, Object>> fetchOne(final String query,
            final Map<String, Object> parameters) {
        LOGGER.debug("Query:\n{}", query);

        // Data is fetched and mapped
        return client.query(query)
            .bindAll(parameters)
            .fetch()
            .first();
    }

    private final Long count(final Statement statement) {
        final String countQuery;

        countQuery = getCountQuery(statement.getCypher());

        LOGGER.debug("Count: {}", countQuery);

        return client.query(countQuery)
            .fetchAs(Long.class)
            .first()
            .get();
    }

    private final String getCountQuery(final String subquery) {
        final SymbolicName name;
        final OngoingReadingAndReturn call;

        name = Cypher.name("value");
        call = Cypher.call("apoc.cypher.run")
            .withArgs(Cypher.literalOf(subquery), Cypher.mapOf())
            .yield(name)
            .returning(Functions.count(name));

        return call.build()
            .getCypher();
    }

    private final String getOrder(final Order order) {
        return String.format("%s %s", order.getProperty(),
            order.getDirection());
    }

}