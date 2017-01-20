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

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Functions {

    private Functions() {
        throw new UnsupportedOperationException("Cannot create instance.");
    }

    public static class ExecutionException extends RuntimeException {
        ExecutionException(Throwable cause) {
            super(cause);
        }
    }

    @FunctionalInterface
    public interface ExConsumer<T> {
        void accept(T t) throws Throwable;
    }

    @NotNull
    @Contract("null->fail")
    public static <T> Consumer<T> consumer(@NotNull @NonNull ExConsumer<T> ec) {
        //noinspection Contract
        return t -> {
            try {
                ec.accept(t);
            } catch (Throwable throwable) {
                throw new ExecutionException(throwable);
            }
        };
    }

    @FunctionalInterface
    public interface ExFunction<A, B> {
        B apply(A a) throws Throwable;
    }

    @NotNull
    @Contract("null->fail")
    public static <A, B>Function<A, B> function(@NotNull @NonNull ExFunction<A, B> ef) {
        //noinspection Contract
        return a -> {
            try {
                return ef.apply(a);
            } catch (Throwable throwable) {
                throw new ExecutionException(throwable);
            }
        };
    }
}
