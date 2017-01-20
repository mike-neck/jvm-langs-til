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

import com.example.TypedCellProcessor;
import org.supercsv.util.CsvContext;

public enum Country {

    JAPAN
    , UK
    , ISRAEL
    , ENGLAND;

    public static class CsvProcessor extends TypedCellProcessor<Country> {

        public CsvProcessor() {
            super(Country.class);
        }

        @Override
        protected String convert(Country obj, CsvContext context) {
            final String name = obj.name();
            final String h = name.substring(0, 1);
            final String t = name.substring(1).toLowerCase();
            return h + t;
        }

        @Override
        protected Country convert(String str, CsvContext context) {
            return Country.valueOf(str.toUpperCase());
        }
    }
}
