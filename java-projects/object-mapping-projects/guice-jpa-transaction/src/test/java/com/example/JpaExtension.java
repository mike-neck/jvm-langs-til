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
import com.google.inject.persist.PersistService;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Parameter;

public class JpaExtension implements BeforeAllCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create("guice-jpa-transaction");

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        store.getOrComputeIfAbsent(Injector.class, c -> Guice.createInjector(new TestModule()), Injector.class)
                .getInstance(PersistService.class)
                .start();
    }

    @Override
    public boolean supports(ParameterContext pcx,
            ExtensionContext ecx) throws ParameterResolutionException {
        final Parameter parameter = pcx.getParameter();
        return parameter.isAnnotationPresent(InjectBean.class);
    }

    @Override
    public Object resolve(ParameterContext pcx,
            ExtensionContext ecx) throws ParameterResolutionException {
        final Parameter parameter = pcx.getParameter();
        final Class<?> type = parameter.getType();
        final ExtensionContext.Store store = ecx.getStore(NAMESPACE);
        final Injector injector = store.get(Injector.class, Injector.class);
        return injector.getInstance(type);
    }
}
