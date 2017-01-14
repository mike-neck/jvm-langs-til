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
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Resource {

    private final ClassLoader loader = getClass().getClassLoader();

    private static final Resource instance = new Resource();

    private Resource() {}

    public static Resource getInstance() {
        return instance;
    }

    public Reader getReader(@NotNull @NonNull String res) throws IOException {
        return getReader(res, StandardCharsets.UTF_8);
    }

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    public Reader getReader(@NotNull @NonNull String name, @NotNull @NonNull Charset cs) throws IOException {
        return Optional.ofNullable(loader.getResourceAsStream(name))
                .map(s -> new InputStreamReader(s, cs))
                .orElseThrow(() -> new FileNotFoundException("resource [" + name + "] not found."));
    }

    @NotNull
    @Contract("null->fail")
    public InputStream getInputStream(@NotNull @NonNull String name) throws IOException {
        return Optional.ofNullable(loader.getResourceAsStream(name))
                .orElseThrow(() -> new FileNotFoundException("resource [" + name + "] not found."));
    }
}
