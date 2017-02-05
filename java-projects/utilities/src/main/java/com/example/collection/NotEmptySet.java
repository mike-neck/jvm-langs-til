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
package com.example.collection;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class NotEmptySet<E> {

    private final Set<E> elements;

    @SafeVarargs
    @SuppressWarnings("ConstantConditions")
    public NotEmptySet(@NotNull @NonNull E... elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("expecting more than 1 element, but there is no element.");
        }
        final Set<E> set = new HashSet<>(elements.length);
        for (E e : elements) {
            if (e == null) {
                throw new IllegalArgumentException("elements cannot accept null.");
            }
            set.add(e);
        }
        this.elements = set;
    }

    public static <E> NotEmptySet<E> fromSet(@NotNull @NonNull Set<E> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("expecting more than 1 element, but there is no element.");
        }
        return new NotEmptySet<>(set);
    }

    private NotEmptySet(@NotNull @NonNull Set<E> set) {
        final Set<E> elements = new HashSet<>(set.size());
        for (E e : set) {
            if (e == null) {
                throw new IllegalArgumentException("elements cannot accept null.");
            }
            elements.add(e);
        }
        this.elements = elements;
    }

    public int size() {
        return elements.size();
    }

    public Set<E> getElements() {
        return elements;
    }
}
