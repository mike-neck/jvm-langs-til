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

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.extension.*;

import javax.persistence.EntityManager;

public class InjectorInitializer implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(InjectorInitializer.class, Injector.class);

    private static final JpaInitialize.Module MODULE = new JpaInitialize.Module();

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        final Injector injector = store.getOrComputeIfAbsent(MODULE, Guice::createInjector, Injector.class);
        final JpaInitialize initializer = store.getOrComputeIfAbsent(JpaInitialize.class, injector::getInstance,
                JpaInitialize.class);
        initializer.init();
    }

    @Override
    public void beforeEach(TestExtensionContext context) throws Exception {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        final Injector injector = store.get(MODULE, Injector.class);
        final JpaInitialize initializer = store.get(JpaInitialize.class, JpaInitialize.class);
        initializer.cleanDb(injector.getInstance(EntityManager.class));
    }

    @Override
    public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        return Injector.class.equals(type);
    }

    @Override
    public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(MODULE);
    }
}
