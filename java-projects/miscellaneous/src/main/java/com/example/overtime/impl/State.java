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
package com.example.overtime.impl;

import com.example.overtime.Queue;
import com.example.overtime.StateOfMonth;
import com.example.overtime.WorkOfMonth;

import java.time.Month;

public final class State {

    private State() {
    }

    private static class Empty implements StateOfMonth {

        private final Month month;

        private Empty(Month month) {
            this.month = month;
        }

        @Override
        public int getOvertime() {
            return 0;
        }

        @Override
        public Month getMonth() {
            return null;
        }

        @Override
        public Queue<WorkOfMonth> getQueue() {
            return null;
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
        public double getTotalOvertimeInThisYear() {
            return 0;
        }

        @Override
        public int getCountOfMonthOver45Hours() {
            return 0;
        }
    }
}
