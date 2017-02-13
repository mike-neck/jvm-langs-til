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

import com.example.data.Tuple;
import com.example.value.Tweet;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.stream.Collectors.toList;

@Data
public class Store {

    private final List<Tuple<LocalDateTime, Tweet>> list = new CopyOnWriteArrayList<>();

    public void add(Tweet tweet) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime twenty = now.minusMinutes(20L);
        final List<Tuple<LocalDateTime, Tweet>> old = list.stream()
                .filter(t -> t.getLeft().isBefore(twenty))
                .collect(toList());
        list.removeAll(old);
        list.add(new Tuple<>(now, tweet));
    }
}
