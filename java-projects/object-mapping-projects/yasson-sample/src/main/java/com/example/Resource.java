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
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Optional;

public final class Resource {

    private final ClassLoader loader = getClass().getClassLoader();

    @NotNull
    public Optional<InputStream> getAsStream(@NotNull @NonNull String name) {
        return getResource(name)
                .map(u -> loader.getResourceAsStream(name));
    }

    @NotNull
    public Optional<Reader> getAsReader(@NotNull @NonNull String name) {
        return getResource(name)
                .map(u -> loader.getResourceAsStream(name))
                .map(InputStreamReader::new);
    }

    @NotNull
    private Optional<URL> getResource(@NotNull @NonNull String name) {
        return Optional.ofNullable(loader.getResource(name));
    }
}
