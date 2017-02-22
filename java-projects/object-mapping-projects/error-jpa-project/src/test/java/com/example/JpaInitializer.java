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

import com.example.work.Start;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.junit.jupiter.api.extension.*;

import java.time.ZoneId;

public class JpaInitializer implements BeforeAllCallback, ParameterResolver, AfterAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(JpaInitializer.class);

    private final Module module = new Module();

    @Override
    public void afterAll(ContainerExtensionContext context) throws Exception {
        context.getStore(NAMESPACE).remove(module, Injector.class).getInstance(PersistService.class).stop();
    }

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        final Injector injector = store.getOrComputeIfAbsent(module, Guice::createInjector, Injector.class);
        injector.getInstance(PersistService.class).start();
    }

    @Override
    public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        return type.equals(Injector.class);
    }

    @Override
    public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        return extensionContext.getStore(NAMESPACE).get(module, Injector.class).getInstance(type);
    }

    public static class Module extends AbstractModule {

        @Override
        protected void configure() {
            install(new JpaPersistModule("error-jpa-sample"));
            bind(ZoneId.class).toProvider(() -> ZoneId.of("Asia/Tokyo"));
            bind(Start.class).to(Work.class);
        }
    }
}
