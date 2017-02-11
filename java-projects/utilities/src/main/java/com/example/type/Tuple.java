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
package com.example.type;

import com.example.function.Functions;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
@RequiredArgsConstructor
public class Tuple<L, R> {

    private final L left;
    private final R right;

    @NotNull
    @Contract("null->fail")
    public <N> Tuple<L, N> map(@NotNull @NonNull Functions.ExFunction<? super R, ? extends N> f) {
        return new Tuple<>(left, Functions.function(f).apply(right));
    }

    @NotNull
    public Tuple<R, L> reverse() {
        return new Tuple<>(right, left);
    }

    @NotNull
    @Contract("null->fail")
    public ConsumeMode<R> consumeLeft(@NotNull @NonNull Functions.ExConsumer<? super L> cl) {
        //noinspection Contract
        return cr -> {
            Functions.consumer(cl).accept(left);
            Functions.consumer(cr).accept(right);
        };
    }

    @FunctionalInterface
    public interface ConsumeMode<R> {
        @Contract("null->fail")
        void consumeRight(@NotNull Functions.ExConsumer<? super R> cr);
    }

    @NotNull
    @Contract("null->fail")
    public static <L, R> Function<L, Tuple<L, R>> mkTuple(
            @NotNull @NonNull Functions.ExFunction<? super L, ? extends R> f) {
        //noinspection Contract
        return l -> new Tuple<>(l, Functions.function(f).apply(l));
    }

    @NotNull
    @Contract("null->fail")
    public static <L, R, N> Function<Tuple<L, R>, Tuple<L, N>> mapTuple(
            @NotNull @NonNull Functions.ExFunction<? super R, ? extends N> f) {
        //noinspection Contract
        return t -> new Tuple<>(t.left, Functions.function(f).apply(t.right));
    }

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    public static <L, R> Consumer<Tuple<L, R>> consumeTuple(@NotNull @NonNull Functions.ExConsumer<? super L> cl,
            @NotNull @NonNull Functions.ExConsumer<? super R> cr) {
        //noinspection Contract
        return t -> t.consumeLeft(cl).consumeRight(cr);
    }

    @NotNull
    @Contract("null->fail")
    public static <L, R> Predicate<Tuple<L, R>> conditionTuple(@NotNull @NonNull Functions.ExPredicate<R> pr) {
        //noinspection Contract
        return t -> Functions.predicate(pr).test(t.right);
    }
}
