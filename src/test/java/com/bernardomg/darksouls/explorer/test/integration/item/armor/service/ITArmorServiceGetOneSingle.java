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

package com.bernardomg.darksouls.explorer.test.integration.item.armor.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;

import com.bernardomg.darksouls.explorer.item.armor.domain.Armor;
import com.bernardomg.darksouls.explorer.item.armor.service.ArmorService;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.context.Neo4jApplicationContextInitializer;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;
import com.bernardomg.darksouls.explorer.test.configuration.db.Neo4jDatabaseInitalizer;

@IntegrationTest
@ContextConfiguration(
        initializers = { ITArmorServiceGetOneSingle.Initializer.class })
@DisplayName("Reading single armor from id")
public class ITArmorServiceGetOneSingle {

    public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(
                final ConfigurableApplicationContext configurableApplicationContext) {
            new Neo4jApplicationContextInitializer(dbContainer)
                .initialize(configurableApplicationContext);
        }
    }

    @Container
    private static final Neo4jContainer<?> dbContainer = ContainerFactory
        .getNeo4jContainer();

    @BeforeAll
    private static void prepareTestdata() {
        new Neo4jDatabaseInitalizer().initialize("neo4j",
            dbContainer.getAdminPassword(), dbContainer.getBoltUrl(),
            Arrays.asList("classpath:db/queries/armor/single.cypher"));
    }

    @Autowired
    private Neo4jClient  client;

    @Autowired
    private ArmorService service;

    /**
     * Default constructor.
     */
    public ITArmorServiceGetOneSingle() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data")
    public void testGetOne_Data() {
        final Armor data;
        final Long id;

        id = getId();

        data = service.getOne(id)
            .get();

        Assertions.assertEquals("Armor name", data.getName());
        Assertions.assertEquals(Arrays.asList("Description"),
            data.getDescription());
    }

    @Test
    @DisplayName("Returns no data for a not existing id")
    public void testGetOne_NotExisting() {
        final Optional<Armor> data;

        data = service.getOne(-1l);

        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("Returns the correct requirements")
    public void testGetOne_Stats() {
        final Armor data;
        final Long id;

        id = getId();

        data = service.getOne(id)
            .get();

        Assertions.assertEquals(5, data.getDurability());
        Assertions.assertEquals(6, data.getWeight());
    }

    private final Long getId() {
        final Collection<Map<String, Object>> rows;

        rows = client.query("MATCH (n:Item) RETURN n")
            .fetch()
            .all();

        return (Long) rows.stream()
            .findFirst()
            .get()
            .getOrDefault("id", 0l);
    }

}
