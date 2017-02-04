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

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> klass, Object id) {
        super("Resource [object: " + klass.getSimpleName() + ", id:" + id + "] not found.");
    }

    @SuppressWarnings("Contract")
    @NotNull
    @Contract("null,_->fail;_,null->fail")
    public static Supplier<NotFoundException> notFound(@NotNull @NonNull Class<?> klass, @NotNull @NonNull Object id) {
        return () -> new NotFoundException(klass, id);
    }
}
