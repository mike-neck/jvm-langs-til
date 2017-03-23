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
package com.example.freee;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class 和暦 {

    private static final JapaneseEra 平成 = JapaneseEra.HEISEI;
    private static final JapaneseEra 明治 = JapaneseEra.MEIJI;

    public static void main(String[] args) {
        final ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendText(ChronoField.ERA)
                .appendText(ChronoField.YEAR_OF_ERA)
                .appendLiteral("年")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral("日")
                .toFormatter(Locale.JAPAN);
        final DateTimeFormatter iso = DateTimeFormatter.ISO_DATE;

        final JapaneseDate now = JapaneseDate.now(tokyo);
        System.out.println(now.format(formatter));

        final JapaneseDate showa = JapaneseDate.of(1989, 1, 7);
        System.out.println(showa.format(formatter));

        final JapaneseDate heisei = JapaneseDate.of(1989, 1, 8);
        System.out.println(heisei.format(formatter));

        System.out.println("---");

        try {
            JapaneseDate.of(1872, 12, 31);
        } catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());
            System.out.println(e.getMessage());
            System.out.println("1872/12/31");
        }

        System.out.println("---");

        final JapaneseDate h = JapaneseDate.of(平成, 29, 1, 1);
        System.out.println(h.format(iso));

        final JapaneseDate m = JapaneseDate.of(明治, 6, 1, 1);
        System.out.println(m.format(iso));

        System.out.println("---");

        try {
            JapaneseDate.of(明治, 5, 12, 31);
        } catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());
            System.out.println(e.getMessage());
            System.out.println("明治5年12月31日");
        }

        System.out.println("---");

        final LocalDate ldm = LocalDate.of(1873, 1, 1);
        System.out.println(JapaneseDate.from(ldm).format(formatter));

        try {
            LocalDate.of(1872, 12, 31);
        } catch (Exception e) {
            System.out.println(e.getClass().getCanonicalName());
            System.out.println(e.getMessage());
            System.out.println("明治5年12月31日");
        }
    }
}
