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

import com.example.module.TestModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.jupiter.api.extension.*;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestInitializer implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace KEY = ExtensionContext.Namespace.create(TestInitializer.class);

    private static final TestModule TEST_MODULE = new TestModule();

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        final Injector injector = context.getStore(KEY)
                .getOrComputeIfAbsent(TEST_MODULE, Guice::createInjector, Injector.class);
        injector.getInstance(AppTest.class);
    }

    @Override
    public void beforeEach(TestExtensionContext context) throws Exception {
        final Injector injector = context.getStore(KEY)
                .get(TEST_MODULE, Injector.class);
        injector.getInstance(DbCleaner.class).runClean();
    }

    @Override
    public void afterAll(ContainerExtensionContext context) throws Exception {
    }

    @Override
    public boolean supports(ParameterContext pxt, ExtensionContext ext)
            throws ParameterResolutionException {
        final Parameter param = pxt.getParameter();
        final Class<?> type = param.getType();
        final List<Annotation> list = Arrays.asList(param.getAnnotations());
        final Key<?> key = key(type, list);

        final ExtensionContext.Store store = ext.getStore(KEY);
        final Injector injector = store.get(TEST_MODULE, Injector.class);

        try {
            final Object obj = store.getOrComputeIfAbsent(key, injector::getInstance);
            return obj != null;
        } catch (ConfigurationException e) {
            return false;
        }
    }

    @Override
    public Object resolve(ParameterContext pxt, ExtensionContext ext)
            throws ParameterResolutionException {
        final Parameter param = pxt.getParameter();
        final Class<?> type = param.getType();
        final List<Annotation> list = Arrays.asList(param.getAnnotations());
        final Key<?> key = key(type, list);

        final ExtensionContext.Store store = ext.getStore(KEY);

        return store.get(key);
    }

    private static Key<?> key(Class<?> type, List<Annotation> list) {
        final Optional<Annotation> named = list.stream()
                .filter(funAndPredicate(Annotation::getClass, sameClass(Named.class)))
                .findFirst();
        final Optional<Annotation> guiceNamed = list.stream()
                .filter(funAndPredicate(Annotation::getClass, sameClass(com.google.inject.name.Named.class)))
                .findFirst();
        final Optional<Annotation> qualifier = list.stream()
                .filter(funAndPredicate(compose(Annotation::getClass, Class::getAnnotations).andThen(Arrays::asList), 
                        contains(Qualifier.class)))
                .findFirst();

        if (named.isPresent()) {
            return Key.get(type, named.get());
        } else if (guiceNamed.isPresent()) {
            return Key.get(type, guiceNamed.get());
        } else if (qualifier.isPresent()) {
            return Key.get(type, qualifier.get());
        } else {
            return Key.get(type);
        }
    }

    private static Predicate<List<Annotation>> contains(Class<? extends Annotation> klass) {
        Objects.requireNonNull(klass);
        return list -> {
            for (Annotation a : list) {
                if (a.getClass().equals(klass)) {
                    return true;
                }
            }
            return false;
        };
    }

    private static <A, B, C> Function<A, C> compose(Function<A, B> f, Function<B, C> g) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(g);
        return a -> g.apply(f.apply(a));
    }

    private static <A, B> Predicate<A> funAndPredicate(Function<A, B> f, Predicate<B> p) {
        Objects.requireNonNull(f);
        Objects.requireNonNull(p);

        return a -> p.test(f.apply(a));
    }

    private static Predicate<Class<?>> sameClass(Class<?> klass) {
        return c -> c.equals(klass);
    }
}
