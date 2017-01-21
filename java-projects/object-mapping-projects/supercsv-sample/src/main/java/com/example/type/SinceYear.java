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
package com.example.type;

import com.example.processor.TypedCellProcessor;
import com.example.sample3.CellProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import java.time.LocalDate;
import java.time.Month;

@RequiredArgsConstructor
public class SinceYear implements Wrapper<LocalDate> {

    private final LocalDate value;

    public SinceYear(int year) {
        this(LocalDate.of(year, Month.JANUARY, 1));
    }

    @Override
    public LocalDate getValue() {
        return value;
    }

    public static class Processor extends TypedCellProcessor<SinceYear> {

        public Processor() {
            super(SinceYear.class);
        }

        public Processor(CellProcessor next) {
            super(SinceYear.class, next);
        }

        @NotNull
        @Override
        protected String convert(SinceYear obj, CsvContext context) {
            return String.format("%d", obj.value.getYear());
        }

        @Override
        protected SinceYear convert(String str, CsvContext context) {
            if (str.isEmpty()) {
                return null;
            } else {
                return new SinceYear(Integer.parseInt(str));
            }
        }

        public static class Factory implements CellProcessorFactory<SinceYear> {
            @Override
            public TypedCellProcessor<SinceYear> processor() {
                return new Processor();
            }
        }
    }
}
