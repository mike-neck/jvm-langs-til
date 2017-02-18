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

import com.example.annotation.Algorithm;
import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.stream.Stream;

@Singleton
public class JpaInitialize {

    private final PersistService service;

    private final ClassLoader loader = getClass().getClassLoader();

    @Inject
    public JpaInitialize(PersistService service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        service.start();
    }

    @PreDestroy
    public void end() {
        service.stop();
    }

    @Transactional
    void cleanDb(EntityManager em) {
        try (Stream<String> st = new BufferedReader(
                new InputStreamReader(loader.getResourceAsStream("delete.sql"), StandardCharsets.UTF_8)).lines()) {
            st.map(em::createNativeQuery)
                    .forEach(Query::executeUpdate);
        }
    }

    public static class Module extends AbstractModule {
        @Override
        protected void configure() {
            install(new JpaPersistModule("jpa-mapping-example"));
            bind(ZoneId.class).toProvider(() -> ZoneId.of("Asia/Tokyo"));
            bind(String.class).annotatedWith(Algorithm.class).toInstance("SHA-512");
        }
    }
}
