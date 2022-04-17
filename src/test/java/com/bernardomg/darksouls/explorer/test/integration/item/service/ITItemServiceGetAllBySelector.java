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

import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;

import com.bernardomg.darksouls.explorer.item.domain.Item;
import com.bernardomg.darksouls.explorer.item.request.DefaultItemRequest;
import com.bernardomg.darksouls.explorer.item.service.ItemService;
import com.bernardomg.darksouls.explorer.persistence.model.DisabledPagination;
import com.bernardomg.darksouls.explorer.persistence.model.DisabledSort;
import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.context.Neo4jApplicationContextInitializer;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;
import com.bernardomg.darksouls.explorer.test.configuration.db.Neo4jDatabaseInitalizer;

@IntegrationTest
@ContextConfiguration(
        initializers = { ITItemServiceGetAllBySelector.Initializer.class })
@DisplayName("Reading items by selector")
public class ITItemServiceGetAllBySelector {

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
            Arrays.asList("classpath:db/queries/item/additional_tag.cypher"));
    }

    @Autowired
    private ItemService service;

    /**
     * Default constructor.
     */
    public ITItemServiceGetAllBySelector() {
        super();
    }

    @Test
    @DisplayName("Filtering by item returns all the data")
    public void testGetAll_ByItem_Count() {
        final Iterable<Item> data;
        final DefaultItemRequest request;
        final Collection<String> selectors;

        request = new DefaultItemRequest();

        selectors = Arrays.asList("item");
        request.setSelectors(selectors);

        data = service.getAll(request, new DisabledPagination(),
            new DisabledSort());

        Assertions.assertEquals(1, IterableUtils.size(data));
    }

    @Test
    @DisplayName("Filtering by item returns all the data")
    public void testGetAll_ByItem_Data() {
        final Item data;
        final DefaultItemRequest request;
        final Collection<String> selectors;

        request = new DefaultItemRequest();

        selectors = Arrays.asList("item");
        request.setSelectors(selectors);

        data = service
            .getAll(request, new DisabledPagination(), new DisabledSort())
            .iterator()
            .next();

        Assertions.assertEquals("Item name", data.getName());
        Assertions.assertEquals(Arrays.asList("Description"),
            data.getDescription());
        Assertions.assertEquals(Arrays.asList("Item", "Tag"), data.getTags());
    }

    @Test
    @DisplayName("Filtering by weapon returns no data")
    public void testGetAll_ByWeapon_Count() {
        final Iterable<Item> data;
        final DefaultItemRequest request;
        final Collection<String> selectors;

        request = new DefaultItemRequest();

        selectors = Arrays.asList("weapon");
        request.setSelectors(selectors);

        data = service.getAll(request, new DisabledPagination(),
            new DisabledSort());

        Assertions.assertEquals(0, IterableUtils.size(data));
    }

}
