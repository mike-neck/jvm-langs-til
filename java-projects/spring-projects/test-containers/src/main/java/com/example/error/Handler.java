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
package com.example.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Handler {

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
