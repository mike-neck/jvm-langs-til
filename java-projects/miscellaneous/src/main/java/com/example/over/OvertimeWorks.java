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
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static com.example.Tuple.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class OvertimeWorks {

    public interface OvertimeWork {
        Month getMonth();

        int getHours();

        default boolean is45HoursOver() {
            return getHours() >= 45;
        }

        OvertimeWork nextMonth(int hour);
    }

    public enum Term {
        LATEST(1),
        TWO_MONTH(2),
        THREE_MONTH(3),
        FOUR_MONTH(4),
        FIVE_MONTH(5);

        private final int length;

        Term(int length) {
            this.length = length;
        }

        @Contract(pure = true)
        public int getSkip() {
            return 12 - length;
        }

        public int getSkip(int maxLength) {
            if (maxLength < this.length) {
                return 0;
            } else {
                return maxLength - this.length;
            }
        }

        public int getLength() {
            return length;
        }
    }

    public interface WorkSummary {
        int getMonths();

        List<OvertimeWork> getRaw();

        OvertimeWork getLatest();

        default List<OvertimeWork> get5Month() {
            final List<OvertimeWork> raw = getRaw();
            final int skip = raw.size() < 5 ? 0 : raw.size() - 5;
            return raw.stream().skip(skip).collect(toList());
        }

        default int countOver45Hours() {
            return getRaw().stream().filter(OvertimeWork::is45HoursOver).mapToInt(x -> 1).sum();
        }

        int countOver45HoursIn11Month();

        Map<Term, Integer> getSumByTerm();

        int getTotal();

        Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour);

        static WorkSummary empty(Month month) {
            return new Empty(month);
        }
    }

    public static WorkSummary nextMonthPlan(WorkSummary previous) {
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

    @Data
    @AllArgsConstructor
    public static class Work implements OvertimeWork {
        private Month month;
        private int hours;

        @Override
        public OvertimeWork nextMonth(int hour) {
            return new Work(month.plus(1), hour);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Work{");
            sb.append("month=").append(month.getDisplayName(TextStyle.FULL, Locale.JAPAN));
            sb.append(", hours=").append(hours);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class Empty implements WorkSummary {

        private final Month month;

        private Empty(Month month) {
            this.month = month;
        }

        @Override
        public int getMonths() {
            return month.getValue();
        }

        @Override
        public List<OvertimeWork> getRaw() {
            return Collections.emptyList();
        }

        @Override
        public OvertimeWork getLatest() {
            throw new UnsupportedOperationException("empty::latest");
        }

        @Override
        public int countOver45HoursIn11Month() {
            return 0;
        }

        @Override
        public Map<Term, Integer> getSumByTerm() {
            return Arrays.stream(Term.values())
                    .map(mkTuple(t -> 0))
                    .collect(toMap(Tuple::getLeft, Tuple::getRight));
        }

        @Override
        public int getTotal() {
            return 0;
        }

        @Override
        public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
            final Incomplete sum = new Incomplete(new Work(month.plus(1), hour));
            return new Tuple<>(sum, Optional.empty());
        }
    }

    private static class Incomplete implements WorkSummary {

        private final List<OvertimeWork> works;

        private Incomplete(OvertimeWork work) {
            this.works = new ArrayList<>(12);
            this.works.add(work);
        }

        private Incomplete(List<OvertimeWork> works) {
            this.works = works;
        }

        @Override
        public int getMonths() {
            return works.size();
        }

        @Override
        public List<OvertimeWork> getRaw() {
            return Collections.unmodifiableList(works);
        }

        @Override
        public OvertimeWork getLatest() {
            return works.get(works.size() - 1);
        }

        @Override
        public int countOver45HoursIn11Month() {
            return countOver45Hours();
        }

        @Override
        public Map<Term, Integer> getSumByTerm() {
            return Arrays.stream(Term.values())
                    .map(mkTuple(t -> t.getSkip(works.size())))
                    .map(mapTuple(s -> works.stream().skip(s)))
                    .map(mapTuple(s -> s.mapToInt(OvertimeWork::getHours)))
                    .map(mapTuple(IntStream::sum))
                    .collect(toMap(Tuple::getLeft, Tuple::getRight));
        }

        @Override
        public int getTotal() {
            return works.stream().mapToInt(OvertimeWork::getHours).sum();
        }

        @Override
        public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
            final OvertimeWork work = works.get(works.size() - 1).nextMonth(hour);
            final List<OvertimeWork> list = new ArrayList<>(works);
            list.add(work);
            if (list.size() == 12) {
                return new Tuple<>(new Full(list), Optional.empty());
            } else {
                return new Tuple<>(new Incomplete(list), Optional.empty());
            }
        }
    }

    private static class Full implements WorkSummary {

        private final List<OvertimeWork> works;

        private Full(List<OvertimeWork> works) {
            this.works = works;
        }

        @Override
        public int getMonths() {
            return 12;
        }

        @Override
        public List<OvertimeWork> getRaw() {
            return Collections.unmodifiableList(works);
        }

        @Override
        public OvertimeWork getLatest() {
            return works.get(works.size() - 1);
        }

        @Override
        public int countOver45HoursIn11Month() {
            return works.stream().skip(1).filter(OvertimeWork::is45HoursOver).mapToInt(o -> 1).sum();
        }

        @Override
        public Map<Term, Integer> getSumByTerm() {
            return Arrays.stream(Term.values())
                    .map(mkTuple(Term::getSkip))
                    .map(mapTuple(s -> works.stream().skip(s)))
                    .map(mapTuple(s -> s.mapToInt(OvertimeWork::getHours)))
                    .map(mapTuple(IntStream::sum))
                    .collect(toMap(Tuple::getLeft, Tuple::getRight));
        }

        @Override
        public int getTotal() {
            return works.stream().skip(0).mapToInt(OvertimeWork::getHours).sum();
        }

        @Override
        public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
            final OvertimeWork work = works.get(11).nextMonth(hour);
            final List<OvertimeWork> next = new ArrayList<>(works.subList(1, 12));
            next.add(work);
            return new Tuple<>(new Full(next), Optional.of(works.get(0)));
        }
    }
}
