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
package com.example.sample2;

import com.example.type.Country;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static com.example.Functions.consumer;

public class Main {

    public static void main(String[] args) {
        final StringWriter sw = new StringWriter();
        try(final CsvDozerBeanWriter cw = new CsvDozerBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE)) {
            cw.configureBeanMapping(Artist.class, Artist.MAPPER);
            cw.writeHeader(Artist.HEADER);
            artists().forEach(consumer(a -> cw.write(a, Artist.PROCESSORS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sw);
    }

    private static List<Artist> artists() {
        return Arrays.asList(
                new Artist(1, "Hallucinogen", Country.UK, 1990, 1998, null, null)
                , new Artist(2, "ケン・イシイ", Country.JAPAN, 1998, 2000, "Sony", 1)
                , new Artist(3, "Infected Mushroom", Country.ISRAEL, 1996, null, "EMC", 2)
        );
    }
}
