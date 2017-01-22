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
package com.example.sample3;

import com.example.type.Country;
import com.example.type.EndYear;
import com.example.type.Id;
import com.example.type.Label;
import com.example.type.Members;
import com.example.type.Name;
import com.example.type.SinceYear;
import org.jetbrains.annotations.NotNull;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntFunction;

public class Main {

    public static void main(String[] args) throws IOException {
        final Writer writer = new StringWriter();
        try (final CsvDozerBeanWriter cw = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE)) {
            final CsvWriter<Artist> w = new CsvWriter<>(Artist.class);
            w.write(artists()).to(cw);
        }
        System.out.println(writer);
    }

    private static List<Artist> artists() {
        return Arrays.asList(
                id(1).name("Hallucinogen").country(Country.UK)
                        .since(1995).end(2005).emptyLabel().emptyMembers()
                , id(2).name("Infected Mushroom").country(Country.ISRAEL)
                        .since(1996).emptyEnd().label("BNE").members(2)
                , id(3).name("Juno Reactor").country(Country.ENGLAND)
                        .since(1993).emptyEnd().emptyLabel().emptyMembers()
        );
    }

    private static <T> Optional<T> map(
            IntFunction<T> f
            , @SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalInt oi) {
        if (oi.isPresent()) {
            return Optional.ofNullable(f.apply(oi.getAsInt()));
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    public static NameBld id(int i) {
        return n -> c -> s -> oe -> ol -> om -> new Artist(
                new Id(i)
                , new Name(n)
                , c
                , new SinceYear(s)
                , map(EndYear::new, oe)
                , ol.map(Label::new)
                , map(Members::new, om)
        );
    }

    public interface NameBld {
        CountryBld name(String n);
    }

    public interface CountryBld {
        SinceBld country(Country c);
    }

    public interface SinceBld {
        EndBld since(int s);
    }

    public interface EndBld {
        LabelBld end(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalInt e);
        default LabelBld end(int e) {
            return end(OptionalInt.of(e));
        }
        default LabelBld emptyEnd() {
            return end(OptionalInt.empty());
        }
    }

    public interface LabelBld {
        MembersBld label(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<String> ol);
        default MembersBld label(String l) {
            return label(Optional.of(l));
        }
        default MembersBld emptyLabel() {
            return label(Optional.empty());
        }
    }

    public interface MembersBld {
        Artist members(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") OptionalInt om);
        default Artist members(int m) {
            return members(OptionalInt.of(m));
        }
        default Artist emptyMembers() {
            return members(OptionalInt.empty());
        }
    }
}
