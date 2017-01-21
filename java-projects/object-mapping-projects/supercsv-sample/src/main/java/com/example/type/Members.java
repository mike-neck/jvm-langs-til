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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

@RequiredArgsConstructor
public class Members implements Wrapper<Integer> {

    private final int value;

    @Override
    public Integer getValue() {
        return value;
    }

    public static class Processor extends TypedCellProcessor<Members> {
        public Processor() {
            super(com.example.type.Members.class);
        }

        public Processor(CellProcessor next) {
            super(com.example.type.Members.class, next);
        }

        @NotNull
        @Override
        protected String convert(Members obj, CsvContext context) {
            return String.format("%d", obj.value);
        }

        @Override
        protected Members convert(String str, CsvContext context) {
            if (str.isEmpty()) {
                return new Members(0);
            }
            return new Members(Integer.parseInt(str));
        }
    }
}
