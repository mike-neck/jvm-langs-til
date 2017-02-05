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

import org.junit.jupiter.api.Test;

import java.util.*;

import static com.example.collection.NotEmptySet.fromSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotEmptySetTest {

    @SuppressWarnings("unchecked")
    @Test
    void throwsExceptionWhenNoArgumentIsGivenToConstructor() {
        assertThrows(IllegalArgumentException.class, NotEmptySet::new);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void throwsExceptionWhenNullIsGivenToConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new NotEmptySet<String>(null));
    }

    @Test
    void throwsExceptionWhenArgumentsContainsNull() {
        assertThrows(IllegalArgumentException.class, () -> new NotEmptySet<>("aaa", "b", null, "dd"));
    }

    @Test
    void throwsNothingWhenOneParameterIsGiven() {
        final NotEmptySet<String> set = new NotEmptySet<>("test");
        assertNotNull(set);
        assertEquals(1, set.size());
    }

    @Test
    void throwsNothingWhenAllParameterIsNotNull() {
        final NotEmptySet<String> set = new NotEmptySet<>("aaa", "b", "cc");
        assertNotNull(set);
        assertEquals(3, set.size());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void fromSetThrowsExceptionWhenNullIsGiven() {
        assertThrows(IllegalArgumentException.class, () -> fromSet(null));
    }

    @Test
    void fromSetThrowsExceptionWhenSetContainsNull() {
        assertThrows(IllegalArgumentException.class, () -> fromSet(setOf("aaa", "b", null, "dd")));
    }

    @Test
    void fromSetThrowsNothingWhenNonEmptyNoNullContainedSetIsGiven() {
        final NotEmptySet<String> set = fromSet(setOf("aaa", "b", "cc"));
        assertNotNull(set);
        assertEquals(3, set.size());
    }

    @SafeVarargs
    private static <E> Set<E> setOf(E... items) {
        final List<E> list = new ArrayList<>();
        Collections.addAll(list, items);
        return new HashSet<>(list);
    }
}
