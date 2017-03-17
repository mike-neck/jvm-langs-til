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
package com.example.overtime.impl;

import com.example.overtime.First;
import com.example.overtime.Last;

import java.util.Optional;
import java.util.function.Supplier;

public final class OptionalExt {

    private OptionalExt() {
    }

    private static final class FirstImpls {
        private FirstImpls() {
        }

        private static class Empty<E> implements First<E> {

            @Override
            public E get() {
                throw new UnsupportedOperationException("first::empty::get");
            }

            @Override
            public Optional<E> asOptional() {
                return Optional.empty();
            }

            @Override
            public First<E> or(Supplier<Optional<E>> other) {
                return new HasItem<>(other);
            }
        }

        private static class HasItem<E> implements First<E> {

            private final Supplier<Optional<E>> maybeValue;

            private HasItem(Supplier<Optional<E>> maybeValue) {
                this.maybeValue = maybeValue;
            }

            @Override
            public E get() {
                final Optional<E> e = maybeValue.get();
                return e.orElseThrow(() -> new IllegalStateException("first::get"));
            }

            @Override
            public Optional<E> asOptional() {
                return maybeValue.get();
            }

            @Override
            public First<E> or(final Supplier<Optional<E>> other) {
                return new HasItem<>(() -> maybeValue.get()
                        .map(Optional::of)
                        .orElseGet(other));
            }
        }
    }

    private static final class LastImpls {
        private LastImpls() {
        }

        private static class Empty<E> implements Last<E> {

            @Override
            public E get() {
                throw new UnsupportedOperationException("last::get");
            }

            @Override
            public Optional<E> asOptional() {
                return Optional.empty();
            }

            @Override
            public Last<E> or(Supplier<Optional<E>> other) {
                return new HasItem<>(other);
            }
        }

        private static class HasItem<E> implements Last<E> {

            private final Supplier<Optional<E>> maybeValue;

            private HasItem(Supplier<Optional<E>> maybeValue) {
                this.maybeValue = maybeValue;
            }

            @Override
            public E get() {
                return maybeValue.get().orElseThrow(() -> new IllegalStateException("last::get"));
            }

            @Override
            public Optional<E> asOptional() {
                return maybeValue.get();
            }

            @Override
            public Last<E> or(Supplier<Optional<E>> other) {
                return new HasItem<>(() -> other.get()
                        .map(Optional::of)
                        .orElseGet(maybeValue));
            }
        }
    }
}
