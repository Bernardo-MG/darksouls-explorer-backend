
package com.bernardomg.darksouls.explorer.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.neo4j.cypherdsl.core.ResultStatement;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.cypherdsl.core.StatementBuilder.BuildableStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryExecutor {

    public <T> Page<T> fetch(
            final BuildableStatement<ResultStatement> statementBuilder,
            final Function<Map<String, Object>, T> mapper, final Pageable page);

    public <T> Collection<T> fetch(final String query,
            final Function<Map<String, Object>, T> mapper);

    public <T> Page<T> fetch(final String query,
            final Function<Map<String, Object>, T> mapper, final Pageable page);

    public void run(final Iterable<Statement> statements);

}
