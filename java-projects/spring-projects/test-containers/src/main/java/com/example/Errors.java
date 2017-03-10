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

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
@NoArgsConstructor
class Errors {

    static final Function<FieldError, Message> TRANSFORM_ERROR =
            f -> new Message(f.getField(), f.getRejectedValue());

    private final List<Message> errors = new ArrayList<>();

    Errors add(Message error) {
        this.errors.add(error);
        return this;
    }

    Errors addAll(Errors other) {
        this.errors.addAll(other.errors);
        return this;
    }

    @Data
    static class Message {
        private final String parameter;
        private final String value;
        Message(final String parameter, final Object value) {
            this.parameter = parameter;
            if (value == null) {
                this.value = "<<null>>";
            } else {
                final String s = value.toString();
                this.value = s.isEmpty()? "<<empty>>" : s;
            }
        }
    }
}
