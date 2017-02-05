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

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Optional;

import static com.example.function.Functions.function;

public final class Resource {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(@NonNls String message) {
            super(message);
        }

        public NotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotFoundException(Throwable cause) {
            super(cause);
        }
    }

    private static final Resource INSTANCE = new Resource();

    private final ClassLoader loader = getClass().getClassLoader();

    private Resource() {
    }

    private Optional<InputStream> getInputStream(@NotNull @NonNull String name) {
        return Optional.ofNullable(loader.getResource(name))
                .map(u -> loader.getResourceAsStream(name));
    }

    private Reader getReader(@NotNull @NonNull String name) {
        return getInputStream(name)
                .map(function(i -> new InputStreamReader(i, "UTF-8")))
                .orElseThrow(() -> new NotFoundException("resource [" + name + "] not found"));
    }

    @NotNull
    @Contract("null -> fail")
    public static Reader reader(@NotNull @NonNull String name) {
        return INSTANCE.getReader(name);
    }

    @NotNull
    @Contract("null -> fail")
    public static InputStream getInput(@NotNull @NonNull String name) {
        return INSTANCE.getInputStream(name)
                .orElseThrow(() -> new NotFoundException("resource [" + name + "] not found"));
    }

    public static void copyStream(InputStream i, OutputStream o) throws IOException {
        byte[] buf = new byte[8192];
        int len;
        while ((len = i.read(buf)) > 0) {
            o.write(buf, 0, len);
        }
    }

    public static void copy(@NotNull @NonNull String name, OutputStream o) throws IOException {
        try (InputStream i = getInput(name)) {
            copyStream(i, o);
        }
    }
}
