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

package com.bernardomg.darksouls.explorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.bernardomg.darksouls.explorer.request.argument.PaginationArgumentResolver;
import com.bernardomg.darksouls.explorer.request.argument.SortArgumentResolver;

/**
 * Request configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration
public class RequestConfig {

    /**
     * Default constructor.
     */
    public RequestConfig() {
        super();
    }

    @Bean("paginationArgumentResolver")
    public HandlerMethodArgumentResolver getPaginationArgumentResolver() {
        return new PaginationArgumentResolver();
    }

    @Bean("sortArgumentResolver")
    public HandlerMethodArgumentResolver getSortArgumentResolver() {
        return new SortArgumentResolver();
    }

}