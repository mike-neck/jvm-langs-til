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

import com.example.provider.AlgorithmProvider;
import com.example.provider.MessageDigestProvider;
import com.example.service.SystemTimeZoneService;
import com.google.inject.AbstractModule;

import java.security.MessageDigest;
import java.time.ZoneId;

public class ProviderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ZoneId.class).toProvider(SystemTimeZoneService.class);
        bind(MessageDigestProvider.Algorithm.class).toProvider(AlgorithmProvider.class);
        bind(MessageDigest.class).toProvider(MessageDigestProvider.class);
    }
}
