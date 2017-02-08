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
package com.example.exception;

import com.example.exception.type.BadRequest;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BadRequestException extends RuntimeException {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = 6711961449032923461l;

    private final String process;

    private final BadRequest reason;

    public BadRequestException(Class<?> klass, String process, BadRequest reason, Object causeObj) {
        super("BadRequest at " + klass.getSimpleName() + " process: " + process + "/ Reason [" + reason.getMessage(
                causeObj) + "]");
        this.process = process;
        this.reason = reason;
    }

    public String getProcess() {
        return process;
    }

    public BadRequest getReason() {
        return reason;
    }

    @NotNull
    public static Supplier<BadRequestException> badRequest(@NotNull @NonNull Class<?> klass,
            @NotNull @NonNull String process, @NotNull @NonNull BadRequest reason, @NotNull @NonNull Object cause) {
        return () -> new BadRequestException(klass, process, reason, cause);
    }
}
