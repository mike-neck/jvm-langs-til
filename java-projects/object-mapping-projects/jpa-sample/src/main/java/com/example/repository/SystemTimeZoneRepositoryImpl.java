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
package com.example.repository;

import com.example.entity.SystemTimeZone;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Optional;

@Singleton
public class SystemTimeZoneRepositoryImpl implements SystemTimeZoneRepository {

    private final EntityManager em;

    @Inject
    public SystemTimeZoneRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public SystemTimeZone save(SystemTimeZone zone) {
        em.persist(zone);
        return zone;
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public Optional<SystemTimeZone> findByZoneId(@NotNull @NonNull String zoneId) {
        return Optional.ofNullable(em.find(SystemTimeZone.class, zoneId));
    }
}
