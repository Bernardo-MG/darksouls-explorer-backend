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

package com.bernardomg.darksouls.explorer.test.integration.graph.query;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.bernardomg.darksouls.explorer.Application;
import com.bernardomg.darksouls.explorer.graph.model.Graph;
import com.bernardomg.darksouls.explorer.graph.query.GraphQueries;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;

/**
 * Integration tests for the {@link GraphQueries}.
 */
@IntegrationTest
@Testcontainers
@ContextConfiguration(
        initializers = { ITGraphQueriesByLinkTypeNoData.Initializer.class })
@SpringBootTest(classes = Application.class)
@DisplayName("Querying the repository filtering by type with no data")
public class ITGraphQueriesByLinkTypeNoData {

    public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(
                final ConfigurableApplicationContext configurableApplicationContext) {

            neo4jContainer.addExposedPorts(7687);
            TestPropertyValues
                    .of("spring.neo4j.uri=" + neo4jContainer.getBoltUrl(),
                            "spring.neo4j.authentication.username=neo4j",
                            "spring.neo4j.authentication.password="
                                    + neo4jContainer.getAdminPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Container
    private static final Neo4jContainer<?> neo4jContainer = ContainerFactory
            .getNeo4jContainer();

    @Autowired
    private GraphQueries                   queries;

    /**
     * Default constructor.
     */
    public ITGraphQueriesByLinkTypeNoData() {
        super();
    }

    @Test
    @DisplayName("Returns no data")
    public void testFindAllByLinkType_Count() {
        final Graph data;

        data = queries.findAllByLinkType(Arrays.asList("RELATIONSHIP", "ABC"));

        Assertions.assertEquals(0, IterableUtils.size(data.getLinks()));
        Assertions.assertEquals(0, IterableUtils.size(data.getNodes()));
        Assertions.assertEquals(0, IterableUtils.size(data.getTypes()));
    }

    @Test
    @DisplayName("Returns no data for an empty type list")
    public void testFindAllByLinkType_Empty_Count() {
        final Graph data;

        data = queries.findAllByLinkType(Collections.emptyList());

        Assertions.assertEquals(0, IterableUtils.size(data.getLinks()));
        Assertions.assertEquals(0, IterableUtils.size(data.getNodes()));
        Assertions.assertEquals(0, IterableUtils.size(data.getTypes()));
    }

}
