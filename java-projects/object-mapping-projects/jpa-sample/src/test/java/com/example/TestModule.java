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

import com.example.modules.RepositoryModule;
import com.example.modules.ServiceModule;
import com.example.repository.SystemTimeZoneRepository;
import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import java.time.ZoneId;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("jpa-sample"));
        bind(ZoneId.class).toProvider(SystemTimeZoneRepository.class);
        install(new RepositoryModule());
        install(new ServiceModule());
    }
}
