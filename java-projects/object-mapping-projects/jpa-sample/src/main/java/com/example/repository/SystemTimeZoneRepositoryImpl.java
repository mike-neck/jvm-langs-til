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
import com.google.inject.persist.Transactional;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.time.ZoneId;

@Singleton
public class SystemTimeZoneRepositoryImpl implements SystemTimeZoneRepository {

    private static final String DEFAULT_TIME_ZONE = "Asia/Tokyo";

    private final EntityManager em;

    @Inject
    public SystemTimeZoneRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @NotNull
    @Transactional
    @Override
    public ZoneId getSystemTimeZone() {
        final SystemTimeZone zone = em.find(SystemTimeZone.class, DEFAULT_TIME_ZONE);
        if (zone != null) {
            return zone.getZoneId();
        } else {
            final SystemTimeZone stz = new SystemTimeZone(DEFAULT_TIME_ZONE);
            em.persist(stz);
            em.flush();
            return stz.getZoneId();
        }
    }

    @NotNull
    @Override
    public ZoneId get() {
        return getSystemTimeZone();
    }
}
