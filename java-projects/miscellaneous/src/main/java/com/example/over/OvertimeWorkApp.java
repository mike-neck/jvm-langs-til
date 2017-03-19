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
package com.example.over;

import com.example.Tuple;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static com.example.Tuple.biMapTuple;
import static com.example.Tuple.toTuple;

public final class OvertimeWorkApp {

    public static WorkSummary nextMonthPlan(WorkSummary previous) {
        final int total = previous.getTotalIn11Month();
        if (720 - total < 44) {
            return previous.next(720 - total).getLeft();
        }
        final int over45 = previous.countOver45HoursIn11Month();
        if (over45 == 6) {
            return previous.next(44).getLeft();
        }
        final int next = previous.getSumByTerm().entrySet().stream()
                .map(toTuple())
                .map(biMapTuple((l, r) -> l.getLength() * 80 + 80 - 1 - r))
                .mapToInt(Tuple::getRight)
                .map(i -> Integer.min(99, i))
                .min()
                .orElse(0);
        return previous.next(next).getLeft();
    }

    public static void main(String[] args) {
        final WorkSummary empty = WorkSummary.empty(Month.DECEMBER);
        final WorkSummary jan = nextMonthPlan(empty);
        System.out.println(jan.getLatest());
        final WorkSummary feb = nextMonthPlan(jan);
        System.out.println(feb.getLatest());
        final WorkSummary mar = nextMonthPlan(feb);
        System.out.println(mar.getLatest());

        final BiFunction<WorkSummary, Month, WorkSummary> func = (WorkSummary w, Month m) -> nextMonthPlan(w);
        final List<WorkSummary> result = foldl(Arrays.asList(Month.values()), WorkSummary.empty(Month.DECEMBER), func);
        result.stream()
                .map(WorkSummary::getLatest)
                .forEach(System.out::println);
        System.out.println(String.format("Total: [%d]", result.get(11).getTotal()));
        foldl(Arrays.asList(Month.values()), result.get(11), func)
                .stream()
                .map(WorkSummary::getLatest)
                .forEach(System.out::println);
    }

    private static List<WorkSummary> foldl(List<Month> months, WorkSummary empty, BiFunction<? super WorkSummary, ? super Month, ? extends WorkSummary> f) {
        final List<WorkSummary> result = new ArrayList<>(months.size());
        WorkSummary pre = empty;
        for (Month month : months) {
            pre = f.apply(pre, month);
            result.add(pre);
        }
        return result;
    }
}
