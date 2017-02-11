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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ExOptional<T extends Throwable, E> {

    @NotNull <R> ExOptional<T, R> map(@NotNull Function<? super E, ? extends R> function);

    @NotNull <R> ExOptional<T, R> flatMap(
            @NotNull Function<? super E, ? extends Optional<R>> function,
            @NotNull Function<? super E, ? extends T> handler);

    @NotNull
    E getOrThrow() throws T;

    static <T extends Throwable, E> ExOptional<T, E> of(
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") @NotNull @NonNull Optional<E> optional,
            @NotNull @NonNull Supplier<T> supplier) {
        return optional
                .<ExOptional<T, E>>map(Some::new)
                .orElse(new Nothing<>(supplier));
    }
}

class Some<T extends Throwable, E> implements ExOptional<T, E> {

    private final E item;

    Some(E item) {
        this.item = item;
    }

    @NotNull
    @Contract("null->fail")
    @Override
    public <R> ExOptional<T, R> map(@NotNull @NonNull Function<? super E, ? extends R> function) {
        return new Some<>(function.apply(item));
    }

    @Contract("null,_->fail;_,null->fail")
    @NotNull
    @Override
    public <R> ExOptional<T, R> flatMap(
            @NotNull @NonNull Function<? super E, ? extends Optional<R>> function,
            @NotNull @NonNull Function<? super E, ? extends T> handler) {
        return function.apply(item)
                .<ExOptional<T, R>>map(Some::new)
                .orElse(new Nothing<>(() -> handler.apply(item)));
    }

    @NotNull
    @Override
    public E getOrThrow() throws T {
        return item;
    }
}

class Nothing<T extends Throwable, E> implements ExOptional<T, E> {

    private final Supplier<T> exception;

    Nothing(Supplier<T> exception) {
        this.exception = exception;
    }

    @NotNull
    @Override
    public <R> ExOptional<T, R> map(@NotNull Function<? super E, ? extends R> function) {
        return new Nothing<>(exception);
    }

    @NotNull
    @Override
    public <R> ExOptional<T, R> flatMap(
            @NotNull Function<? super E, ? extends Optional<R>> function,
            @NotNull Function<? super E, ? extends T> handler) {
        return new Nothing<>(exception);
    }

    @NotNull
    @Override
    public E getOrThrow() throws T {
        throw exception.get();
    }
}
