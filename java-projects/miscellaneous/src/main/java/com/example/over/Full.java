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

import java.util.*;
import java.util.stream.IntStream;

import static com.example.Tuple.mapTuple;
import static com.example.Tuple.mkTuple;
import static java.util.stream.Collectors.toMap;

class Full implements WorkSummary {

    private final List<OvertimeWork> works;

    Full(List<OvertimeWork> works) {
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
    public int getTotalIn11Month() {
        return works.stream().skip(1).mapToInt(OvertimeWork::getHours).sum();
    }

    @Override
    public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
        final OvertimeWork work = works.get(11).nextMonth(hour);
        final List<OvertimeWork> next = new ArrayList<>(works.subList(1, 12));
        next.add(work);
        return new Tuple<>(new Full(next), Optional.of(works.get(0)));
    }
}
