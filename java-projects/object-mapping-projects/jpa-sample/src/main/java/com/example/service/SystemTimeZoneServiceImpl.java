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
package com.example.service;

import com.example.entity.SystemTimeZone;
import com.example.repository.SystemTimeZoneRepository;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.exception.NotFoundException.notFound;

@Singleton
public class SystemTimeZoneServiceImpl implements SystemTimeZoneService {

    private static final String DEFAULT_TIME_ZONE = "Japan";

    private static final List<String> DEFAULT_ZONES = Arrays.asList(
            "CET",
            "CST6CDT",
            "Cuba",
            "EET",
            "EST5EDT",
            "Egypt",
            "Eire",
            "GB",
            "GB-Eire",
            "GMT",
            "GMT0",
            "Greenwich",
            "Hongkong",
            "Iceland",
            "Iran",
            "Israel",
            "Jamaica",
            "Japan",
            "Kwajalein",
            "Libya",
            "MET",
            "MST7MDT",
            "NZ",
            "NZ-CHAT",
            "Navajo",
            "PRC",
            "PST8PDT",
            "Poland",
            "Portugal",
            "ROK",
            "Singapore",
            "Turkey",
            "UCT",
            "UTC",
            "Universal",
            "W-SU",
            "WET",
            "Zulu"
    );

    private final SystemTimeZoneRepository repository;

    @Inject
    public SystemTimeZoneServiceImpl(SystemTimeZoneRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ZoneId getDefaultTimeZone() {
        final Optional<SystemTimeZone> zone = repository.findByZoneId(DEFAULT_TIME_ZONE);
        if (!zone.isPresent()) {
            DEFAULT_ZONES.stream()
                    .map(SystemTimeZone::new)
                    .forEach(repository::save);
            return repository.findByZoneId(DEFAULT_TIME_ZONE)
                    .map(SystemTimeZone::getZoneId)
                    .orElseThrow(notFound(SystemTimeZone.class, DEFAULT_TIME_ZONE));
        } else {
            return zone.get().getZoneId();
        }
    }
}
