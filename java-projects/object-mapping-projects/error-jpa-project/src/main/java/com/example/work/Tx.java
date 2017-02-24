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
package com.example.work;

import static com.example.function.Functions.consumer;

import com.example.function.Functions;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public interface Tx<T> {

    <R> Tx<R> transaction(Functions.ExBiFunction<? super EntityManager, ? super T, ? extends R> script)
            throws Throwable;

    @NotNull
    Optional<T> finish();

    @Contract("null -> fail")
    default void andFinally(@NotNull @NonNull Functions.ExConsumer<T> consumer) throws Throwable {
        finish().ifPresent(consumer(consumer));
    }

    class Impl<T> implements Tx<T> {

        private final EntityManager em;
        private final T value;

        public Impl(EntityManager em, T value) {
            this.em = em;
            this.value = value;
        }

        @Override
        public <R> Tx<R> transaction(Functions.ExBiFunction<? super EntityManager, ? super T, ? extends R> script)
                throws Throwable {
            final EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                final R newValue = script.apply(em, value);
                tx.commit();
                return new Impl<>(em, newValue);
            } catch (Throwable throwable) {
                tx.rollback();
                throw throwable;
            }
        }

        @NotNull
        @Override
        public Optional<T> finish() {
            return Optional.of(value);
        }
    }
}
