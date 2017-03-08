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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TodoNotFoundException extends RuntimeException {

    TodoNotFoundException(final Long id) {
        super(String.format("Todo item is not found. id = %d", id));
    }

    @ControllerAdvice
    public static class Handler {

        @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
        @ExceptionHandler({ TodoNotFoundException.class })
        public ResponseEntity<ErrorMessage> error(final TodoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body(new ErrorMessage(404, e.getMessage()));
        }

        @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
        @ExceptionHandler({ BadHttpRequest.class })
        public ResponseEntity<ErrorMessage> errorMessage(final BadHttpRequest e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body(new ErrorMessage(400, e.getMessage()));
        }
    }

    static class BadHttpRequest extends RuntimeException {
        BadHttpRequest(final String paramName, final Object value) {
            super(badRequestMessage(paramName, value));
        }

        private static String badRequestMessage(String paramName, Object value) {
            return String.format("Bad parameter [%s]. value = %s", paramName, value);
        }
    }

    static final Function<FieldError, Errors.Message> TRANSFORM_ERROR =
            f -> new Errors.Message(f.getField(), f.getRejectedValue());

    @Data
    @RequiredArgsConstructor
    static class ErrorMessage {
        private final int httpStatus;
        private final String message;
    }

    @Data
    @NoArgsConstructor
    static class Errors {
        private final List<Message> errors = new ArrayList<>();
        Errors add(Message error) {
            this.errors.add(error);
            return this;
        }
        Errors addAll(Errors other) {
            this.errors.addAll(other.errors);
            return this;
        }
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
}
