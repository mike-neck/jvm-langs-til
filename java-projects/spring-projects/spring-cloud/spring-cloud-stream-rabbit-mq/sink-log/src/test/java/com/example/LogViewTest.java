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
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LogViewTest {

    private LogView logView;

    private Listener listener;

    @Before
    public void setup() {
        Store store = new Store();
        List<Tuple<LocalDateTime, Tweet>> list = store.getList();
        this.logView = new LogView(store);
        this.listener = new Listener(store);

        final LocalDateTime now = LocalDateTime.now();
        list.add(new Tuple<>(now.minusMinutes(20L), new Tweet("20 min before.")));
        list.add(new Tuple<>(now.minusMinutes(19L), new Tweet("19 min before.")));
    }

    @Test
    public void addThenView_Returns2Items() {
        listener.listen(new Tweet("test"));
        final List<Tweet> tweets = logView.viewAll();
        assertThat(tweets.size(), is(2));
    }
}
