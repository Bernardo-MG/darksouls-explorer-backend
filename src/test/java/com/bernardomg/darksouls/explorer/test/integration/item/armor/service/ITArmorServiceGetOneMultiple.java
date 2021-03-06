/**
 * Copyright 2021-2022 the original author or authors
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import com.bernardomg.darksouls.explorer.item.armor.domain.Armor;
import com.bernardomg.darksouls.explorer.item.armor.repository.ArmorRepository;
import com.bernardomg.darksouls.explorer.item.armor.service.ArmorService;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;

@IntegrationTest
@DisplayName("Reading single armor from id with multiple data")
@Sql({ "/db/queries/armor/multiple.sql" })
public class ITArmorServiceGetOneMultiple {

    @Container
    private static final MySQLContainer<?> mysqlContainer = ContainerFactory.getMysqlContainer();

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
    }

    @Autowired
    private ArmorRepository repository;

    @Autowired
    private ArmorService    service;

    /**
     * Default constructor.
     */
    public ITArmorServiceGetOneMultiple() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data")
    public void testGetOne_Data() {
        final Armor data;
        final Long  id;

        id = getId();

        data = service.getOne(id)
            .get();

        Assertions.assertEquals("Armor 1", data.getName());
        Assertions.assertEquals("Description 1", data.getDescription());
    }

    @Test
    @DisplayName("Returns the correct requirements")
    public void testGetOne_Stats() {
        final Armor data;
        final Long  id;

        id = getId();

        data = service.getOne(id)
            .get();

        Assertions.assertEquals(5, data.getDurability());
        Assertions.assertEquals(6, data.getWeight());
    }

    private final Long getId() {
        return repository.findAll()
            .iterator()
            .next()
            .getId();
    }

}
