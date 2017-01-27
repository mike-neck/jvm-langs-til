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

import com.example.model.Task;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.StringWriter;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        final Jsonb jsonb = JsonbBuilder.create();
        final StringWriter writer = new StringWriter();
        jsonb.toJson(task(), writer);
        System.out.println(writer);
    }

    @NotNull
    @Contract(" -> !null")
    private static Task task() {
        return new Task("JSONB"
                , 1
                , "create json"
                , "create json literal by yasson"
                , "mike"
                , LocalDate.now());
    }
}
