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
package com.example.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    private String text;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithResults {

        private boolean result;
        // TODO 本当はLocalDateTimeでやりたい
        private String time;
        private Tweet tweet;

        private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        public WithResults(boolean result, LocalDateTime time, Tweet tweet) {
            this.result = result;
            this.time = time.format(ISO);
            this.tweet = tweet;
        }
    }
}
