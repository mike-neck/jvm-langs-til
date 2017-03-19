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
import java.util.*;

import static com.example.Tuple.mkTuple;
import static java.util.stream.Collectors.toMap;

class Empty implements WorkSummary {

    private final Month month;

    Empty(Month month) {
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
    public int getTotalIn11Month() {
        return 0;
    }

    @Override
    public Tuple<WorkSummary, Optional<OvertimeWork>> next(int hour) {
        final Incomplete sum = new Incomplete(new Work(month.plus(1), hour));
        return new Tuple<>(sum, Optional.empty());
    }
}
