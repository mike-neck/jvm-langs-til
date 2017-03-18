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

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class OvertimeWorks {

    public interface OvertimeWork {
        Month getMonth();

        int getHours();

        default boolean is45HoursOver() {
            return getHours() >= 45;
        }

        OvertimeWork nextMonth(int hour);
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

        default int count45HoursOver() {
            return getRaw().stream().filter(OvertimeWork::is45HoursOver).mapToInt(x -> 1).sum();
        }

        double get2MonthAverage();

        double get6MonthAverage();

        double getYearlyAverage();

        int getTotal();

        Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour);

        static WorkSummary empty(Month month) {
            return new Empty(month);
        }
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
        public double get2MonthAverage() {
            return 0;
        }

        @Override
        public double get6MonthAverage() {
            return 0;
        }

        @Override
        public double getYearlyAverage() {
            return 0;
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
        public double get2MonthAverage() {
            return monthAverage(2);
        }

        private double monthAverage(int m) {
            if (m <= 0) {
                throw new IllegalArgumentException("work-summary::average::month(<= 0)");
            }
            if (works.size() < m) {
                final int sum = getTotal();
                return sum / m;
            } else {
                return works.stream().skip(works.size() - m).mapToInt(OvertimeWork::getHours).average().orElse(0D);
            }
        }

        @Override
        public double get6MonthAverage() {
            return monthAverage(6);
        }

        @Override
        public double getYearlyAverage() {
            return monthAverage(12);
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
        public double get2MonthAverage() {
            return getAverage(skippedStream(10));
        }

        @Override
        public double get6MonthAverage() {
            return getAverage(skippedStream(6));
        }

        @Override
        public double getYearlyAverage() {
            return getAverage(skippedStream(0));
        }

        @Override
        public int getTotal() {
            return skippedStream(0).mapToInt(OvertimeWork::getHours).sum();
        }

        @Override
        public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
            final OvertimeWork work = works.get(11).nextMonth(hour);
            final List<OvertimeWork> next = new ArrayList<>(works.subList(1, 12));
            next.add(work);
            return new Tuple<>(new Full(next), Optional.of(works.get(0)));
        }

        private Stream<OvertimeWork> skippedStream(int n) {
            return works.stream().skip(n);
        }

        private double getAverage(final Stream<OvertimeWork> stream) {
            return stream.mapToInt(OvertimeWork::getHours).average().orElse(0D);
        }
    }
}
