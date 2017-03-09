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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.example.App.defaultTodoItems;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { App.class })
@WebAppConfiguration
public class TodoControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(TodoControllerTest.class);

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    private MockMvc mvc;

    private HttpMessageConverter jackson2HttpMessageConverter;

    @Autowired
    private TodoRepository repository;
    @Autowired
    private WebApplicationContext context;
    private List<Todo> todos;

    @Autowired
    void setConverters(HttpMessageConverter<?>... converters) {
        this.jackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
        assumeNotNull(jackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        mvc = webAppContextSetup(context).build();
        repository.deleteAllInBatch();
        final List<Todo> list = Arrays.stream(defaultTodoItems())
                .map(p -> new Todo(p.getTitle(), p.getDescription()))
                .collect(toList());
        todos = repository.save(list);
        todos.forEach(t -> logger.info(String.format("created todo item[id: %d, title: %s]", t.getId(), t.getTitle())));
    }

    @Test
    public void found() throws Exception {
        mvc.perform(get("/todo/" + todos.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.title", is("Testcontainersのことを調べる")));
    }
}
