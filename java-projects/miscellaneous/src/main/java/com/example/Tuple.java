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

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Data(staticConstructor = "tuple")
@RequiredArgsConstructor
public class Tuple<L, R> {

    private final L left;
    private final R right;

    @NotNull
    public static <K, V> Function<Map.Entry<K, V>, Tuple<K, V>> toTuple() {
        return e -> new Tuple<K, V>(e.getKey(), e.getValue());
    }

    @NotNull
    public static <L, R> Function<L, Tuple<L, R>> mkTuple(@NonNull final Function<? super L, ? extends R> f) {
        return l -> new Tuple<L, R>(l, f.apply(l));
    }

    @NotNull
    public static <L, R, N> Function<Tuple<L, R>, Tuple<L, N>> mapTuple(@NonNull final Function<? super R, ? extends N> f) {
        return t -> new Tuple<L, N>(t.left, f.apply(t.right));
    }

    @NotNull
    public static <L, R, N> Function<Tuple<L, R>, Tuple<L, N>> biMapTuple(@NonNull final BiFunction<? super L, ? super R, ? extends N> f) {
        return t -> new Tuple<L, N>(t.left, f.apply(t.left, t.right));
    }
}
