
package com.bernardomg.darksouls.explorer.problem.query;

import java.util.Map;

import com.bernardomg.darksouls.explorer.problem.model.DataProblem;
import com.bernardomg.darksouls.explorer.problem.model.ImmutableDataProblem;
import com.bernardomg.persistence.executor.Query;

public final class MissingRelationshipQuery implements Query<DataProblem> {

    private final String           error = "missing_relationship";

    private final Iterable<String> relationships;

    private final String           source;

    public MissingRelationshipQuery(final String source, final Iterable<String> relationships) {
        super();

        this.source = source;
        this.relationships = relationships;
    }

    @Override
    public final DataProblem getOutput(final Map<String, Object> record) {
        return new ImmutableDataProblem((String) record.getOrDefault("id", ""), source, error);
    }

    @Override
    public final String getStatement(final Map<String, Object> params) {
        final String template;
        final String mergedRels;

        mergedRels = String.join("|", relationships);

        template =
        // @formatter:off
                "MATCH" + System.lineSeparator()
                + "  (n:%s)" + System.lineSeparator()
                + "WHERE" + System.lineSeparator()
                + "  NOT ()-[:%s]->(n)" + System.lineSeparator()
                + "RETURN" + System.lineSeparator()
                + "  n.name AS id";
        return String.format(template, source, mergedRels);
    }

}
