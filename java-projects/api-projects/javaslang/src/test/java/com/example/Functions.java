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

import javaslang.Function1;
import javaslang.control.Option;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Functions {

    @Test
    void composition() {
        final Function1<char[], String> toString = String::new;
        final Function1<String, DateTimeFormatter> toFormatter = DateTimeFormatter::ofPattern;
        final Function1<DateTimeFormatter, Function1<LocalDate, String>> formatter = f -> f::format;

        final Function1<char[], Function1<LocalDate, String>> fun1 = toString.andThen(toFormatter).andThen(formatter);
        final Function1<char[], Function1<LocalDate, String>> fun2 = formatter.compose(toFormatter).compose(toString);

        final char[] chars = {'u', 'u', 'u', 'u', '/', 'M', 'M', '/', 'd', 'd'};
        final Function1<LocalDate, String> f1 = fun1.apply(chars);
        final Function1<LocalDate, String> f2 = fun2.apply(chars);

        final LocalDate localDate = LocalDate.of(2017, 3, 26);
        final String res1 = f1.apply(localDate);
        final String res2 = f2.apply(localDate);

        assertEquals("2017/03/26", res1);
        assertEquals(res1, res2);
    }

    @Test
    void optionalLifting() {
        final Function1<Integer, Integer> mod10 = i -> 10 / i;

        final Function1<Integer, Option<Integer>> liftedMod10 = Function1.lift(mod10);

        final Option<Integer> result0 = liftedMod10.apply(0);
        final Option<Integer> result1 = liftedMod10.apply(1);

        assertTrue(result0.isEmpty());
        assertTrue(result1.isDefined());
    }
}
