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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class WithTransaction {

    private final EntityManager em;

    @Inject
    public WithTransaction(EntityManager em) {
        this.em = em;
    }

    @Transactional
    void run(final Consumer<EntityManager> action) {
        Objects.requireNonNull(action).accept(em);
    }

    @Transactional
    void refreshRun(final Consumer<EntityManager> action) {
        em.clear();
        Objects.requireNonNull(action).accept(em);
    }

    @Transactional
    <T> T call(final Function<? super EntityManager, ? extends T> action) {
        return Objects.requireNonNull(action).apply(em);
    }
}
