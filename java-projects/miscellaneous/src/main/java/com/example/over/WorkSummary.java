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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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
