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
package com.example.sample6;

import com.example.Resource;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        final RecentCustomer rc = RecentCustomer.builder()
                .date(LocalDate.now().minusDays(1))
                .customers(
                        Arrays.asList(
                                Company.builder()
                                        .name("山本商事")
                                        .representative("山本さん")
                                        .address(Address.builder()
                                                .prefecture(Prefecture.CHIBA)
                                                .city("千葉市")
                                                .address("花見川区")
                                                .street("1")
                                                .build())
                                .build()
                                , Person.builder()
                                        .name("佐藤さん")
                                        .birth(LocalDate.of(1977, 2, 10))
                                        .address(Address.builder()
                                                .prefecture(Prefecture.SAITAMA)
                                                .city("さいたま市")
                                                .address("大宮")
                                                .street("2-1")
                                                .build())
                                        .build()
                        ))
                .build();
        System.out.println(marshal(rc));

        System.out.println();

        try (Reader r = Resource.getInstance().getReader("com/example/sample6/customers.xml")) {
            final RecentCustomer c = JAXB.unmarshal(r, RecentCustomer.class);
            System.out.println(c.getDate());
            c.getCustomers().forEach(System.out::println);
        }
    }

    @NotNull
    @Contract(value = "_ -> !null", pure = true)
    private static <T> String marshal(@NotNull @NonNull T obj) {
        final StringWriter w = new StringWriter();
        JAXB.marshal(obj, w);
        return w.toString();
    }
}
