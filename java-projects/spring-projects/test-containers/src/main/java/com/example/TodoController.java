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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequestMapping("todo")
@RestController
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @RequestMapping(value = "{todoId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Todo getTodo(@PathVariable("todoId") final Long todoId) {
        if (todoId == null) {
            throw new BadHttpRequest("todo_id", "<null>");
        }
        return service.findTodo(todoId);
    }

    @RequestMapping(method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> create(@RequestBody @Validated final CreateTodoParameter parameter, final BindingResult result) {
        if (result.hasErrors()) {
            final Errors messages = result.getFieldErrors()
                    .stream()
                    .map(Errors.TRANSFORM_ERROR)
                    .collect(Errors::new,
                            Errors::add,
                            Errors::addAll);
            return ResponseEntity.badRequest()
                    .body(messages);
        }
        final Todo todo = service.createTodo(parameter.getTitle(), parameter.getDescription());
        return ResponseEntity.ok(todo);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateTodoParameter {
        @NotNull
        @Size(min = 1, max = 127)
        private String title;
        @NotNull
        @Size(max = 512)
        private String description;
    }
}
