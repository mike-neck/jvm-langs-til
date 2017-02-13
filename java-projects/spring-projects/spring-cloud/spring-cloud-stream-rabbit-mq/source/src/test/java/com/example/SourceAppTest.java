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

import com.example.value.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SourceAppTest {

    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "WeakerAccess"})
    @Autowired
    ResController controller;

    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "WeakerAccess"})
    @Autowired
    MessageCollector collector;

    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "WeakerAccess"})
    @Autowired
    Source source;

    @Test
    public void testPayload() {
        final Tweet tweet = new Tweet("test-message");
        final Tweet.WithResults results = controller.accept(tweet);

        @SuppressWarnings("unchecked")
        final Message<String> message = (Message<String>) collector.forChannel(source.output()).poll();
        assertThat(message.getPayload()).isInstanceOf(String.class);
        assertThat(message.getPayload()).isEqualTo("{\"text\":\"test-message\"}");
        assertThat(message.getHeaders().get("contentType").toString()).isEqualTo("application/json;charset=UTF-8");
    }
}
