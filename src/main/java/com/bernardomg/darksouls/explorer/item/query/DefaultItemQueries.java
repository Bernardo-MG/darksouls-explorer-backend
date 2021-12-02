
package com.bernardomg.darksouls.explorer.item.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public final Page<Item> findAll(final Pageable page) {
        return queryExecutor.fetch(
        // @formatter:off
              "MATCH" + System.lineSeparator()
            + "  (i:Item)" + System.lineSeparator()
            + "RETURN" + System.lineSeparator()
            + "  id(i) AS id, i.name AS name, i.description AS description",
       // @formatter:on
            this::toItem, page);
    }

    @Override
    public final Page<ItemSource> findSources(final Long id,
            final Pageable page) {
        final Map<String, Object> params;
        final Collection<String> rels;
        final String queryTemplate;
        final String query;

        params = new HashMap<>();
        params.put("id", id);

        rels = Arrays.asList("DROPS", "SELLS", "STARTS_WITH", "REWARDS",
            "CHOSEN_FROM", "ASCENDS", "LOOT", "CHOSEN_FROM");

        queryTemplate =
        // @formatter:off
            "MATCH" + System.lineSeparator()
          + "  (s)-[rel:%s]->(i:Item)" + System.lineSeparator()
          + "WHERE" + System.lineSeparator()
          + "  id(i) = $id" + System.lineSeparator()
          + "RETURN" + System.lineSeparator()
          + "  i.name AS item, s.name AS source, type(rel) AS relationship";
        // @formatter:on;

        query = String.format(queryTemplate, String.join("|", rels));
        return queryExecutor.fetch(query, this::toItemSource, params, page);
    }

    private final Item toItem(final Map<String, Object> record) {
        final Long id;
        final String name;
        final Iterable<String> description;

        id = (Long) record.getOrDefault("id", Long.valueOf(-1));
        name = (String) record.getOrDefault("name", "");
        description = Arrays.asList(
            ((String) record.getOrDefault("description", "")).split("\\|"));

        return new DefaultItem(id, name, description);
    }

    private final ItemSource toItemSource(final Map<String, Object> record) {
        final String type;

        switch ((String) record.getOrDefault("relationship", "")) {
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
            default:
                type = "";
        }

        return new DefaultItemSource((String) record.getOrDefault("item", ""),
            (String) record.getOrDefault("source", ""), type);
    }

}
