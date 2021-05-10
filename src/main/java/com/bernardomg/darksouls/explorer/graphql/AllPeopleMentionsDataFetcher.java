/**
 * Copyright 2020 the original author or authors
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

package com.bernardomg.darksouls.explorer.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bernardomg.darksouls.explorer.model.Mention;
import com.bernardomg.darksouls.explorer.persistence.repository.MentionRepository;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Fetcher for the all people query.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Component
public final class AllPeopleMentionsDataFetcher
        implements DataFetcher<Iterable<Mention>> {

    /**
     * Planets repository.
     */
    @Autowired
    private MentionRepository mentionRepository;

    /**
     * Default constructor.
     */
    public AllPeopleMentionsDataFetcher() {
        super();
    }

    @Override
    public Iterable<Mention> get(final DataFetchingEnvironment environment)
            throws Exception {
        return mentionRepository.findAllMentions();
    }

}