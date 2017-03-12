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

import com.example.parameter.CreateTodoParameter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @NotNull
    static CreateTodoParameter[] defaultTodoItems() {
        return new CreateTodoParameter[]{new CreateTodoParameter("Testcontainersのことを調べる", "* SpringBootでの利用例\n* " +
                "プロジェクトで利用できないか\n* JUnit5で使えないか", "mike-neck"),
                new CreateTodoParameter("エンベデッドMySQLのことを調べる", "いろいろ使えないか", "mike-neck")};
    }
}
