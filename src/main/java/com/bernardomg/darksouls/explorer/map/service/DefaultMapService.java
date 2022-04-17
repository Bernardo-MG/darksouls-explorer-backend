
package com.bernardomg.darksouls.explorer.map.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.bernardomg.darksouls.explorer.map.domain.Map;
import com.bernardomg.darksouls.explorer.map.domain.MapConnection;
import com.bernardomg.darksouls.explorer.map.query.AllIMapsQuery;
import com.bernardomg.darksouls.explorer.map.query.AllMapConnectionsQuery;
import com.bernardomg.darksouls.explorer.persistence.executor.QueryExecutor;
import com.bernardomg.darksouls.explorer.persistence.model.PageIterable;
import com.bernardomg.darksouls.explorer.persistence.model.Pagination;
import com.bernardomg.darksouls.explorer.persistence.model.Query;
import com.bernardomg.darksouls.explorer.persistence.model.Sort;

@Component
public final class DefaultMapService implements MapService {

    private final QueryExecutor<String> queryExecutor;

    public DefaultMapService(final QueryExecutor<String> queryExec) {
        super();

        queryExecutor = Objects.requireNonNull(queryExec);
    }

    @Override
    public final PageIterable<Map> getAll(final Pagination pagination,
            final Sort sort) {
        final Query<List<Map>> query;

        query = new AllIMapsQuery();

        return queryExecutor.fetch(query.getStatement(), query::getOutput,
            pagination, Arrays.asList(sort));
    }

    @Override
    public final PageIterable<MapConnection>
            getAllConnections(final Pagination pagination, final Sort sort) {
        final Query<List<MapConnection>> query;

        query = new AllMapConnectionsQuery();

        return queryExecutor.fetch(query.getStatement(), query::getOutput,
            pagination, Arrays.asList(sort));
    }

}
