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
package jvm.langs.til.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Pair<A, B> {

    private final A left;
    private final B right;

    public Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public B getRight() {
        return right;
    }

    public Pair<B, A> reverse() {
        return new Pair<>(right, left);
    }

    public <C> Pair<A, C> map(Function<? super B, ? extends C> func) {
        Objects.requireNonNull(func);
        return new Pair<>(left, func.apply(right));
    }

    public <C> Pair<A, C> bimap(BiFunction<? super A, ? super B, ? extends C> biFunc) {
        Objects.requireNonNull(biFunc);
        return new Pair<>(left, biFunc.apply(left, right));
    }

    public <C> C trans(BiFunction<? super A, ? super B, ? extends C> biFunc) {
        Objects.requireNonNull(biFunc);
        return biFunc.apply(left, right);
    }

    public static <A, B> Function<A, Pair<A, B>> makePair(Function<? super A, ? extends B> f) {
        Objects.requireNonNull(f);
        return a -> new Pair<>(a, f.apply(a));
    }

    public static <A, B> Function<Pair<A, B>, Pair<A, B>> rightConsume(Consumer<? super B> c) {
        Objects.requireNonNull(c);
        return p -> {
            c.accept(p.right);
            return p;
        };
    }

    public static <A, B, C> Function<Pair<A, B>, Pair<A, C>> bimapPair(BiFunction<? super A, ? super B, ? extends C> bf) {
        Objects.requireNonNull(bf);
        return p -> new Pair<>(p.left, bf.apply(p.left, p.right));
    }

    public static <A, B> Function<Pair<A, B>, Pair<A, B>> biConsumePair(BiConsumer<? super A, ? super B> bc) {
        Objects.requireNonNull(bc);
        return p -> {
            bc.accept(p.left, p.right);
            return p;
        };
    }
}
