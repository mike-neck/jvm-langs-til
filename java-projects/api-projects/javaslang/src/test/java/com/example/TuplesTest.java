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

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple8;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("WeakerAccess")
public class TuplesTest {

    @Test
    void createTuple() {
        final Tuple2<String, LocalDate> memo = Tuple.of("foo", LocalDate.of(2017, 3, 21));
        assertEquals("foo", memo._1());
        assertEquals("2017-03-21", memo._2().format(DateTimeFormatter.ISO_DATE));
        assertEquals(2, memo.arity());
    }

    // Haskellの(a,b)におけるmapは map:: (b -> c) -> (a, b) -> (a, c)なので、
    // javaslangのmapは気持ち悪い
    // ジェネリクスの中に型を積んでいくJavaっぽいと言えばJavaっぽい
    // そして当然といえば当然だが、javaslangのTupleはFunctor則を満たさない
    // というか、多分Functorではない
    @Test
    void mapTuple() {
        final Tuple2<String, LocalDate> memo = Tuple.of("uuuu/MM/dd", LocalDate.of(2017, 3, 21));
        final Tuple2<String, LocalDate> result = memo
                .map((f, d) -> new Tuple2<>(DateTimeFormatter.ofPattern(f), d))
                .map((f, d) -> Tuple.of(d.format(f), d));
        assertEquals("2017/03/21", result._1);
    }

    @Test
    void mapTuple4() {
        final Tuple8<String, Long, LocalDate, LocalTime, Character, String, String, ZoneId> tuple8 = Tuple.of("foo", 1L, LocalDate.of(2017, 3, 21), LocalTime.of(22, 35), 'a', "uuuu/MM/dd", "hh:mm", ZoneId.of("Asia/Tokyo"));
        // tuple8のmapが取る関数は
        // (p1,p2,p3,p4,p5,p6,p7,p8) -> (t1,t2,t3,t4,t5,t6,t7,t8)
        // or
        // p1->t1, p2->t2,...p8->t8
    }
}
