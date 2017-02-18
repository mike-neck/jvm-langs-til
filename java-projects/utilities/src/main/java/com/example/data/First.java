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
package com.example.data;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface First<T> extends Monoid<T, First<T>> {

    @NotNull
    T orElse(@NotNull T value);

    @NotNull
    T orElse(@NotNull Supplier<T> defaultValue);

    @NotNull
    First<T> or(@NotNull Supplier<? extends Optional<T>> candidate);

    @NotNull
    First<T> or(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") @NotNull Optional<T> o);

    @NotNull <R> First<R> map(@NotNull Function<? super T, ? extends R> f);

    @Override
    default First<T> append(@NotNull @NonNull First<T> other) {
        return this;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> First<T> of(@NotNull @NonNull Optional<T> candidate) {
        return candidate.<First<T>>map(Already::new)
                .orElseGet(Empty::new);
    }
}

class Empty<T> implements First<T> {

    @NotNull
    @Override
    public T orElse(@NotNull @NonNull T value) {
        return value;
    }

    @Override
    @NotNull
    public T orElse(@NotNull @NonNull Supplier<T> defaultValue) {
        return defaultValue.get();
    }

    @Override
    @NotNull
    public First<T> or(@NotNull @NonNull Supplier<? extends Optional<T>> candidate) {
        return candidate.get()
                .<First<T>>map(Already::new)
                .orElse(this);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @NotNull
    @Override
    public First<T> or(@NotNull @NonNull Optional<T> o) {
        return o.<First<T>>map(Already::new)
                .orElse(this);
    }

    @Override
    @NotNull
    public <R> First<R> map(@NotNull @NonNull Function<? super T, ? extends R> f) {
        return new Empty<>();
    }
}

class Already<T> implements First<T> {

    private final T value;

    Already(@NotNull @NonNull T value) {
        this.value = value;
    }

    @NotNull
    @Override
    public T orElse(@NotNull T value) {
        return this.value;
    }

    @Override
    @NotNull
    public T orElse(@NotNull @NonNull Supplier<T> defaultValue) {
        return value;
    }

    @Override
    @NotNull
    public First<T> or(@NotNull @NonNull Supplier<? extends Optional<T>> candidate) {
        return this;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @NotNull
    @Override
    public First<T> or(@NotNull @NonNull Optional<T> o) {
        return this;
    }

    @Override
    @NotNull
    public <R> First<R> map(@NotNull @NonNull Function<? super T, ? extends R> f) {
        return new Already<>(f.apply(value));
    }
}
