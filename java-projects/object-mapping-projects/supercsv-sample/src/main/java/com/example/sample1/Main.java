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
package com.example.sample1;

import org.jetbrains.annotations.NotNull;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static com.example.function.Functions.consumer;

public class Main {

    public static void main(String[] args) throws IOException {
        final StringWriter w = new StringWriter();
        try (final CsvDozerBeanWriter cw = new CsvDozerBeanWriter(w, CsvPreference.STANDARD_PREFERENCE)) {
            cw.configureBeanMapping(Artist.class, Artist.MAPPER);
            artists().forEach(consumer(a -> cw.write(a, Artist.PROCESSORS)));
        }
        System.out.println(w.toString());
    }

    @NotNull
    private static List<Artist> artists() {
        return Arrays.asList(
                new Artist(1, "Hallucinogen", "UK", 1995, 2005, "Sony", 1)
                , new Artist(2, "Infected Mushroom", "Israel", 1996, null, "BNE", 2)
                , new Artist(3, "Juno Reactor", "Ingland", 1993, null, null, null)
        );
    }
}
