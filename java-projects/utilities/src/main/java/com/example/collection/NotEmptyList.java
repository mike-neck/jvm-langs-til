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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotEmptyList<E> {

    private final List<E> elements;

    @SafeVarargs
    @SuppressWarnings("ConstantConditions")
    public NotEmptyList(@NotNull @NonNull E... elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("expecting more than 1 element, but there is no element.");
        }
        final ArrayList<E> list = new ArrayList<>(elements.length);
        for (E e : elements) {
            if (e == null) {
                throw new IllegalArgumentException("elements cannot accept null.");
            }
            list.add(e);
        }
        this.elements = Collections.unmodifiableList(list);
    }

    public static <E> NotEmptyList<E> fromList(@NotNull @NonNull List<E> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("expecting more than 1 element, but there is no element.");
        }
        return new NotEmptyList<>(list);
    }

    private NotEmptyList(@NotNull @NonNull List<E> list) {
        final ArrayList<E> elements = new ArrayList<>(list.size());
        for (E e : list) {
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

    public List<E> getElements() {
        return elements;
    }
}
