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

package com.bernardomg.darksouls.explorer.test.integration.item.service;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.bernardomg.darksouls.explorer.Application;
import com.bernardomg.darksouls.explorer.graph.query.GraphQueries;
import com.bernardomg.darksouls.explorer.item.model.Item;
import com.bernardomg.darksouls.explorer.item.service.ItemService;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;
import com.bernardomg.darksouls.explorer.test.configuration.db.Neo4jDatabaseInitalizer;
import com.google.common.collect.Iterables;

/**
 * Integration tests for the {@link GraphQueries}.
 */
@IntegrationTest
@Testcontainers
@ContextConfiguration(initializers = { ITItemServiceFindAll.Initializer.class })
@SpringBootTest(classes = Application.class)
@DisplayName("Reading all the items")
public class ITItemServiceFindAll {

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

    @BeforeAll
    private static void prepareTestdata() {
        new Neo4jDatabaseInitalizer().initialize("neo4j",
                neo4jContainer.getAdminPassword(), neo4jContainer.getBoltUrl(),
                Arrays.asList("classpath:db/queries/item/single.cypher"));
    }

    @Autowired
    private ItemService service;

    /**
     * Default constructor.
     */
    public ITItemServiceFindAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the data")
    public void testFindAll_Count() {
        final Iterable<Item> data;

        data = service.getAll(Pageable.unpaged());

        Assertions.assertEquals(1, Iterables.size(data));
    }

    @Test
    @DisplayName("Returns the correct data")
    public void testFindAll_Data() {
        final Item data;

        data = service.getAll(Pageable.unpaged()).iterator().next();

        Assertions.assertEquals("Item", data.getName());
        Assertions.assertEquals("Description", data.getDescription());
    }

}