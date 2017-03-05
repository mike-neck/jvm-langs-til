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
package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateTimeApi {

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("uuuu年MM月dd日 HH:mm:ss.SSS");

    @DisplayName("String ⇔ LocalDateTime")
    @Nested
    class StringToLocalDateTime {

        @DisplayName("String → LocalDateTime")
        @Test
        void stringToLocalDateTime() {
            final LocalDateTime localDateTime = LocalDateTime.parse(
                    "2017-03-04T06:11:20+09:00"
                    , DateTimeFormatter.ISO_DATE_TIME);
            System.out.println(dateTimeFormat.format(localDateTime));
        }

        @DisplayName("String(YYYYMMDDHHMMSS) → LocalDateTime")
        @Test
        void stringToLocalDateTimeWithFormat() {
            final String text = "20170304061120";
            final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendValue(ChronoField.YEAR, 4)
                    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
                    .appendValue(ChronoField.DAY_OF_MONTH, 2)
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                    .toFormatter();
            final LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);
            System.out.println(dateTimeFormat.format(localDateTime));

            final LocalDateTime localDateTime2 = LocalDateTime.parse(text, DateTimeFormatter.ofPattern("uuuuMMddHHmmss"));
            System.out.println(dateTimeFormat.format(localDateTime2));

            assertEquals(localDateTime, localDateTime2);
        }
    }

    @DisplayName("LocalDateTime ⇔ java.util.Date")
    @Nested
    class LocalDateTimeToDate {

        final LocalDateTime localDateTime = LocalDateTime.parse(
                "2017-03-04T13:11:20+09:00"
                , DateTimeFormatter.ISO_DATE_TIME);

        @DisplayName("LocalDateTime → ZonedDateTime(System default zone) → Date")
        @Test
        void localDateTimeToDateWithSystemDefault() {
            final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            final Instant instant = zonedDateTime.toInstant();
            final Date date = Date.from(instant);
            System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
        }

        @DisplayName("LocalDateTime → ZonedDateTime(utc) → Date")
        @Test
        void localDateTimeToDateWithUtc() {
            final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
            final Instant instant = zonedDateTime.toInstant();
            final Date date = Date.from(instant);
            System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
        }

        @DisplayName("Date → ZonedDateTime(systemDefault) → LocalDateTime")
        @Test
        void dateToLocalDateTime() throws ParseException {
            final Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse("20170304131120");
            final Instant instant = date.toInstant();
            final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            final LocalDateTime localDateTime = LocalDateTime.from(zonedDateTime);
            System.out.println(dateTimeFormat.format(localDateTime));
        }

        @DisplayName("Date → ZonedDateTime(utc) → LocalDateTime")
        @Test
        void dateToLocalDateTimeWithUtc() throws ParseException {
            final Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse("20170304131120");
            final Instant instant = date.toInstant();
            final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
            final LocalDateTime localDateTime = LocalDateTime.from(zonedDateTime);
            System.out.println(dateTimeFormat.format(localDateTime));
        }
    }
}
