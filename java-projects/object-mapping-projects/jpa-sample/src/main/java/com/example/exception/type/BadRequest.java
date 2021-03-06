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
package com.example.exception.type;

public enum BadRequest {

    INVALID_VALUE("invalid value: %s")
    , INVALID_NUMBER_OF_PARAMETERS("invalid number of parameters: %s")

    , EMAIL_ALREADY_USED("email already is used by existing account: %s")

    ;

    private final String message;

    BadRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Object value) {
        return String.format(message, value);
    }
}
