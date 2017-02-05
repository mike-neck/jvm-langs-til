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

import java.util.Arrays;

import static com.example.collection.NotEmptyList.fromList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotEmptyListTest {

    @SuppressWarnings("unchecked")
    @Test
    void throwsIllegalArgumentExceptionWhenNoArguments() {
        assertThrows(IllegalArgumentException.class, NotEmptyList::new);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void throwsIllegalArgumentExceptionWhenNullGiven() {
        assertThrows(IllegalArgumentException.class, () -> new NotEmptyList<>(null));
    }

    @Test
    void noExceptionWhenAnParameterGiven() {
        final NotEmptyList<String> list = new NotEmptyList<>("test");
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void throwsIllegalArgumentExceptionWhenParameterHasNull() {
        assertThrows(IllegalArgumentException.class, () -> new NotEmptyList<>("aaa", "b", null, "dd"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void throwsIllegalArgumentExceptionWhenListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fromList(null));
    }

    @Test
    void throwsIllegalArgumentExceptionWhenListContainsNull() {
        assertThrows(IllegalArgumentException.class, () -> fromList(Arrays.asList("aaa", "b", null, "dd")));
    }

    @Test
    void noExceptionWhenNonEmptyListIsGiven() {
        final NotEmptyList<String> list = fromList(Arrays.asList("aaa", "b", "cc"));
        assertNotNull(list);
        assertEquals(3, list.size());
    }
}
