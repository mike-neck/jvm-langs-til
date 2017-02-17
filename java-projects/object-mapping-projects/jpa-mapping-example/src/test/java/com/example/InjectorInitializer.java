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

public class InjectorInitializer implements BeforeAllCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(InjectorInitializer.class, Injector.class);

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        final Injector injector = Guice.createInjector(new JpaInitialize.Module());
        store.put(InjectorInitializer.class, injector);
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
        return extensionContext.getStore(NAMESPACE).get(InjectorInitializer.class);
    }
}
