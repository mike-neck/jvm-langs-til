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
public class Label implements Wrapper<String> {

    private final String value;

    @Override
    public String getValue() {
        return value;
    }

    public static class Processor extends TypedCellProcessor<Label> {

        public Processor() {
            super(Label.class);
        }

        public Processor(CellProcessor next) {
            super(Label.class, next);
        }

        @NotNull
        @Override
        protected String convert(Label obj, CsvContext context) {
            return null;
        }

        @Override
        protected Label convert(String str, CsvContext context) {
            return null;
        }
    }
}
