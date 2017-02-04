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
package com.example.modules;

import com.example.repository.AccountRepository;
import com.example.repository.AccountRepositoryImpl;
import com.example.repository.SystemTimeZoneRepository;
import com.example.repository.SystemTimeZoneRepositoryImpl;
import com.example.repository.TeamRepository;
import com.example.repository.TeamRepositoryImpl;
import com.google.inject.AbstractModule;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SystemTimeZoneRepository.class).to(SystemTimeZoneRepositoryImpl.class);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class);
        bind(TeamRepository.class).to(TeamRepositoryImpl.class);
    }
}
