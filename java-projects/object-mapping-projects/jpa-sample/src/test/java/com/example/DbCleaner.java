/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

public class DbCleaner {

    private final EntityManager em;

    @Inject
    public DbCleaner(EntityManager em) {
        this.em = em;
    }

    @Transactional
    void runClean() throws IOException {
        try(final Stream<String> stream = new BufferedReader(Resource.reader("delete-all.sql")).lines()) {
            stream.map(em::createNativeQuery)
                    .forEach(Query::executeUpdate);
        }
    }
}
