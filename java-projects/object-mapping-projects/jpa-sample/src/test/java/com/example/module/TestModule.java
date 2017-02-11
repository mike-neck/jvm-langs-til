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
package com.example.module;

import com.example.conf.PasswordConfig;
import com.example.modules.ProviderModule;
import com.example.modules.RepositoryModule;
import com.example.modules.ServiceModule;
import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("jpa-sample"));
        install(new RepositoryModule());
        install(new ProviderModule());
        install(new ServiceModule());

        bind(Argon2.class).toProvider(Argon2Factory::create);
        bind(PasswordConfig.class).toInstance(() -> 8192);
    }
}
