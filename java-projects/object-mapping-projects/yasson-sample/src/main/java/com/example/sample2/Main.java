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
package com.example.sample2;

import com.example.Resource;
import com.example.sample2.model.Role;
import com.example.sample2.model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        final Jsonb jsonb = JsonbBuilder.create();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final List<User> u = user();
        jsonb.toJson(u, outputStream);
        final String json = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(json);

        final Optional<InputStream> opStream = new Resource().getAsStream("json/sample2.json");
        try (InputStream stream = opStream.orElseThrow(RuntimeException::new)) {
            //noinspection unchecked
            final List<User> obj = ((List<User>) jsonb.fromJson(stream, List.class));
            System.out.println(obj);
            for (int i = 0; i < 3; i++) {
                System.out.println(u.get(i).equals(obj.get(i)));
                try {
                    final Set<Role> rs = u.get(i).getRoles();
                    final Set<Role> roles = obj.get(i).getRoles();
                    for (Role r : rs) {
                        System.out.println(roles.contains(r));
                    }
                } catch (ClassCastException e) {
                    System.out.println(e);
                }
            }
        }
    }

    @NotNull
    @Contract(" -> !null")
    private static List<User> user() {
        return Arrays.asList(
                new User(
                        1
                        , "mike"
                        , "password"
                        , set(Role.SELF_OPERATION, Role.PROJECT_OPERATION, Role.TEAM_OPERATION)
                )
                , new User(
                        2
                        , "持田"
                        , "password"
                        , set(Role.SELF_OPERATION)
                )
                , new User(
                        3
                        , "(☝՞ਊ ՞)☝ｳｪｰｲwww"
                        , "foo"
                        , set(Role.SYSTEM_ADMIN)
                )
        );
    }

    @SafeVarargs
    @NotNull
    @Contract(pure = true)
    private static <T> Set<T> set(T... values) {
        if (values == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }
}
