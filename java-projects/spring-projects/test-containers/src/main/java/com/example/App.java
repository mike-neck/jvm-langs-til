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

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class App {

    @Bean
    public CommandLineRunner init(final TodoRepository repository) {
        return args -> {
            Stream.of(
                    new TodoController.CreateTodoParameter("Testcontainersのことを調べる", "* SpringBootでの利用例\n* プロジェクトで利用できないか\n* JUnit5で使えないか"),
                    new TodoController.CreateTodoParameter("エンベデッドMySQLのことを調べる", "いろいろ使えないか")
            )
                    .map(p -> new Todo(p.getTitle(), p.getDescription()))
                    .forEach(repository::persist);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
