package com.example;/*
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocalDateApiTest {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu年MM月dd日");

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.SSS");

    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("uuuu年MM月dd日 hh:mm:ss.SSS");

    @DisplayName("String ⇔ LocalDate")
    @Nested
    class StringToLocalDate {

        @DisplayName("String → LocalDate")
        @Test
        void stringToLocalDate() {
            final LocalDate basicIsoDate = LocalDate.parse("20170304", DateTimeFormatter.BASIC_ISO_DATE);
            System.out.println("BASIC_ISO_DATE:" + basicIsoDate.format(dateFormat));
            final LocalDate isoDate = LocalDate.parse("2017-03-04", DateTimeFormatter.ISO_DATE);
            System.out.println("ISO_DATE:" + isoDate.format(dateFormat));
        }
    }

    @DisplayName("LocalDate ⇔ java.util.Date")
    @Nested
    class LocalDateToDate {

        @DisplayName("LocalDate → Date(System default zone)")
        @Test
        void localDateToDate() {
            final LocalDate localDate = LocalDate.parse("20170304", DateTimeFormatter.BASIC_ISO_DATE);
            final Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            final Date date = Date.from(instant);
            System.out.println("LocalDate -> Date(System default zone):" + new SimpleDateFormat("yyyy年MM月dd日").format(date));
        }

        @DisplayName("LocalDate → Date(UTC)")
        @Test
        void localDateToDateWithUtc() {
            final LocalDate localDate = LocalDate.parse("20170304", DateTimeFormatter.BASIC_ISO_DATE);
            final ZonedDateTime utc = localDate.atStartOfDay(ZoneId.of("UTC"));
            final Date date = Date.from(utc.toInstant());
            System.out.println("LocalDate -> Date(UTC):" + new SimpleDateFormat("yyyy年MM月dd日").format(date));
        }

        @DisplayName("Date → LocalDate")
        @Test
        void dateToLocalDate() throws ParseException {
            final Date date = new SimpleDateFormat("yyyyMMdd").parse("20170304");
            final Instant instant = date.toInstant();
            final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            final LocalDate localDate = zonedDateTime.toLocalDate();
            System.out.println("Date -> LocalDate:" + dateFormat.format(localDate));
        }

        @Test
        void whyException() throws ParseException {
            final Date date = new SimpleDateFormat("yyyyMMdd").parse("20170304");
            assertThrows(DateTimeException.class, () -> LocalDate.from(date.toInstant()));
        }
    }
}
