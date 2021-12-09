
package com.bernardomg.darksouls.explorer.item.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.neo4j.cypherdsl.core.Condition;
import org.neo4j.cypherdsl.core.Cypher;
import org.neo4j.cypherdsl.core.Expression;
import org.neo4j.cypherdsl.core.Functions;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.ResultStatement;
import org.neo4j.cypherdsl.core.StatementBuilder.BuildableStatement;
import org.neo4j.cypherdsl.core.StatementBuilder.OngoingReadingWithoutWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import com.bernardomg.darksouls.explorer.item.model.DefaultItem;
import com.bernardomg.darksouls.explorer.item.model.DefaultItemSource;
import com.bernardomg.darksouls.explorer.item.model.Item;
import com.bernardomg.darksouls.explorer.item.model.ItemSource;
import com.bernardomg.darksouls.explorer.persistence.DefaultQueryExecutor;
import com.bernardomg.darksouls.explorer.persistence.QueryExecutor;

@Component
public final class DefaultItemQueries implements ItemQueries {

    private final QueryExecutor queryExecutor;

    @Autowired
    public DefaultItemQueries(final Neo4jClient clnt) {
        super();

        queryExecutor = new DefaultQueryExecutor(clnt);
    }

    @Override
    public final Page<Item> findAll(final String name,
            final Iterable<String> tags, final Pageable page) {
        final Node item;
        final Expression nodeName;
        final OngoingReadingWithoutWhere ongoingBuilder;
        final BuildableStatement<ResultStatement> statementBuilder;
        final String[] additionalLabels;
        final Condition nameCondition;

        additionalLabels = StreamSupport.stream(tags.spliterator(), false)
            .toArray(String[]::new);
        item = Cypher.node("Item", additionalLabels)
            .named("s");
        nodeName = item.property("name");

        ongoingBuilder = Cypher.match(item);

        if (!name.isBlank()) {
            nameCondition = nodeName.matches("(?i).*" + name + ".*");
            ongoingBuilder.where(nameCondition);
        }

        statementBuilder = ongoingBuilder.returning(nodeName.as("name"),
            item.property("description")
                .as("description"),
            Functions.id(item)
                .as("id"),
            Functions.labels(item)
                .as("labels"));

        return queryExecutor.fetch(statementBuilder, this::toItem, page);
    }

    @Override
    public final Page<ItemSource> findSources(final Long id,
            final Pageable page) {
        final Map<String, Object> params;
        final Collection<String> rels;
        final String joinedRels;
        final String queryTemplate;
        final String query;

        params = new HashMap<>();
        params.put("id", id);

        rels = Arrays.asList("DROPS", "SELLS", "STARTS_WITH", "REWARDS",
            "CHOSEN_FROM", "ASCENDS", "LOOT");

        queryTemplate =
        // @formatter:off
            "MATCH" + System.lineSeparator()
          + "  (s)-[rel:%s]->(i:Item)" + System.lineSeparator()
          + "WHERE" + System.lineSeparator()
          + "  id(i) = $id" + System.lineSeparator()
          + "RETURN" + System.lineSeparator()
          + "  i.name AS item, s.name AS source, type(rel) AS relationship";
        // @formatter:on;

        // TODO: Use parameters
        joinedRels = rels.stream()
            .collect(Collectors.joining("|"));
        query = String.format(queryTemplate, joinedRels);
        return queryExecutor.fetch(query, this::toItemSource, params, page);
    }

    private final Item toItem(final Map<String, Object> record) {
        final Long id;
        final String name;
        final Iterable<String> description;
        final Iterable<String> tags;
        final Iterable<String> tagsSorted;

        id = (Long) record.getOrDefault("id", Long.valueOf(-1));
        name = (String) record.getOrDefault("name", "");
        description = Arrays.asList(
            ((String) record.getOrDefault("description", "")).split("\\|"));
        tags = (Iterable<String>) record.getOrDefault("labels",
            Collections.emptyList());
        tagsSorted = StreamSupport.stream(tags.spliterator(), false)
            .sorted()
            .collect(Collectors.toList());

        return new DefaultItem(id, name, description, tagsSorted);
    }

    private final ItemSource toItemSource(final Map<String, Object> record) {
        final String type;
        final String rel;

        rel = (String) record.getOrDefault("relationship", "");

        switch (rel) {
            case "DROPS":
                type = "drop";
                break;
            case "SELLS":
                type = "sold";
                break;
            case "STARTS_WITH":
                type = "starting";
                break;
            case "REWARDS":
                type = "covenant_reward";
                break;
            case "CHOSEN_FROM":
                type = "starting_gift";
                break;
            case "ASCEND":
                type = "ascended";
                break;
            case "LOOT":
                type = "loot";
                break;
            default:
                type = rel;
        }

        return new DefaultItemSource((String) record.getOrDefault("item", ""),
            (String) record.getOrDefault("source", ""), type);
    }

}
