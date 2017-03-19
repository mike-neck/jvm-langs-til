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

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
@AllArgsConstructor
public class Work implements OvertimeWork {
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
