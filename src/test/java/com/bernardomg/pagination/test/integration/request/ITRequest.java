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

package com.bernardomg.pagination.test.integration.request;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;

import com.bernardomg.darksouls.explorer.test.configuration.annotation.IntegrationTest;
import com.bernardomg.darksouls.explorer.test.configuration.context.Neo4jApplicationContextInitializer;
import com.bernardomg.darksouls.explorer.test.configuration.db.ContainerFactory;
import com.bernardomg.pagination.model.Pagination;

@IntegrationTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = { ITRequest.Initializer.class })
@DisplayName("Default request")
@Sql({ "/db/queries/weapon/multiple.sql" })
public class ITRequest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(final ConfigurableApplicationContext configurableApplicationContext) {
            new Neo4jApplicationContextInitializer(neo4jContainer).initialize(configurableApplicationContext);
        }
    }

    @Container
    private static final MySQLContainer<?> mysqlContainer = ContainerFactory.getMysqlContainer();

    @Container
    private static final Neo4jContainer<?> neo4jContainer = ContainerFactory.getNeo4jContainer();

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
    }

    @Autowired
    private MockMvc mockMvc;

    public ITRequest() {
        super();
    }

    @Test
    @DisplayName("Returns data")
    public void request() throws Exception {
        final RequestBuilder request;

        request = MockMvcRequestBuilders.get("/weapons");

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status()
                .isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Weapon 1")));
    }

    @Test
    @DisplayName("Returns default pagination structure")
    public void request_DefaultPagination() throws Exception {
        final RequestBuilder request;

        request = MockMvcRequestBuilders.get("/weapons");

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status()
                .isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber", Matchers.is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size", Matchers.is(Pagination.DEFAULT_SIZE)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.elementsInPage", Matchers.is(5)));
    }

}
