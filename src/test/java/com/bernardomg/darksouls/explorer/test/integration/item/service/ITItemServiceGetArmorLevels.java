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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.IterableUtils;
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

import com.bernardomg.darksouls.explorer.item.domain.ArmorLevel;
import com.bernardomg.darksouls.explorer.item.domain.ArmorProgression;
import com.bernardomg.darksouls.explorer.item.service.ItemService;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.context.Neo4jApplicationContextInitializer;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;
import com.bernardomg.darksouls.explorer.test.configuration.db.Neo4jDatabaseInitalizer;

@IntegrationTest
@ContextConfiguration(
        initializers = { ITItemServiceGetArmorLevels.Initializer.class })
@DisplayName("Reading weapon levels")
public class ITItemServiceGetArmorLevels {

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
            Arrays.asList("classpath:db/queries/armor/armor_2_levels.cypher"));
    }

    @Autowired
    private Neo4jClient client;

    @Autowired
    private ItemService service;

    /**
     * Default constructor.
     */
    public ITItemServiceGetArmorLevels() {
        super();
    }

    @Test
    @DisplayName("Returns the levels protections")
    public void testGetArmorLevels_LevelsProtection() {
        final ArmorProgression data;
        final Iterator<ArmorLevel> levels;
        final Long id;
        ArmorLevel level;

        id = getId();

        data = service.getArmorLevels(id);

        levels = data.getLevels()
            .iterator();

        level = levels.next();
        Assertions.assertEquals(10, level.getRegularProtection());
        Assertions.assertEquals(11, level.getStrikeProtection());
        Assertions.assertEquals(12, level.getSlashProtection());
        Assertions.assertEquals(13, level.getThrustProtection());
        Assertions.assertEquals(14, level.getMagicProtection());
        Assertions.assertEquals(15, level.getFireProtection());
        Assertions.assertEquals(16, level.getLightningProtection());
        Assertions.assertEquals(17, level.getBleedProtection());
        Assertions.assertEquals(18, level.getPoisonProtection());
        Assertions.assertEquals(19, level.getCurseProtection());

        level = levels.next();
        Assertions.assertEquals(20, level.getRegularProtection());
        Assertions.assertEquals(21, level.getStrikeProtection());
        Assertions.assertEquals(22, level.getSlashProtection());
        Assertions.assertEquals(23, level.getThrustProtection());
        Assertions.assertEquals(24, level.getMagicProtection());
        Assertions.assertEquals(25, level.getFireProtection());
        Assertions.assertEquals(26, level.getLightningProtection());
        Assertions.assertEquals(27, level.getBleedProtection());
        Assertions.assertEquals(28, level.getPoisonProtection());
        Assertions.assertEquals(29, level.getCurseProtection());
    }

    @Test
    @DisplayName("Returns the levels in order")
    public void testGetArmorLevels_LevelsOrder() {
        final ArmorProgression data;
        final Iterator<ArmorLevel> levels;
        final Long id;
        ArmorLevel level;

        id = getId();

        data = service.getArmorLevels(id);

        levels = data.getLevels()
            .iterator();

        level = levels.next();
        Assertions.assertEquals(0, level.getLevel());

        level = levels.next();
        Assertions.assertEquals(1, level.getLevel());
    }

    @Test
    @DisplayName("Returns the expected structure")
    public void testGetArmorLevels_Structure() {
        final ArmorProgression data;
        final Long id;

        id = getId();

        data = service.getArmorLevels(id);

        Assertions.assertEquals("Chain Armor", data.getArmor());
        Assertions.assertEquals(2, IterableUtils.size(data.getLevels()));
    }

    private final Long getId() {
        final Collection<Map<String, Object>> rows;

        rows = client.query("MATCH (n:Armor) RETURN n")
            .fetch()
            .all();

        return (Long) rows.stream()
            .findFirst()
            .get()
            .getOrDefault("id", 0l);
    }

}
