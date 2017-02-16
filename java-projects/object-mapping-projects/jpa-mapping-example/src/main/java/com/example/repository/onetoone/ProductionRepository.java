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
package com.example.repository.onetoone;

import com.example.entity.onetoone.ProductionResult;
import com.example.entity.onetoone.ProductionSchedule;
import com.google.inject.persist.Transactional;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProductionRepository {

    private final EntityManager em;
    private final ZoneId zone;

    @Inject
    public ProductionRepository(EntityManager em, ZoneId zone) {
        this.em = em;
        this.zone = zone;
    }

    @Transactional
    public ProductionSchedule saveSchedule(String name) {
        final ProductionSchedule schedule = new ProductionSchedule(name, LocalDateTime.now(zone));
        em.persist(schedule);
        em.flush();
        return schedule;
    }

    public Optional<ProductionSchedule> findByName(String name) {
        return em.createNamedQuery("ProductionSchedule.findByName", ProductionSchedule.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Transactional
    public ProductionResult saveResult(String name) {
        return findByName(name)
                .map(s -> new ProductionResult(s, LocalDateTime.now(zone)))
                .map(peek(em::persist))
                .map(peek(r -> em.flush()))
                .orElseThrow(() -> new RuntimeException("schedule not found for name: " + name));
    }

    @NotNull
    private static <T> Function<T, T> peek(Consumer<? super T> c) {
        return t -> {
            c.accept(t);
            return t;
        };
    }
}
