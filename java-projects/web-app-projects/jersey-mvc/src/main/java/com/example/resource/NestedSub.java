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
package com.example.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Path("nested")
public class NestedSub {

    private static final AtomicInteger userId = new AtomicInteger(0);

    private static final AtomicInteger teamId = new AtomicInteger(0);

    private static final Map<Integer, User> usersMap = new HashMap<>();

    private static final Map<Integer, Team> teamMap = new HashMap<>();

    @NotNull
    private static Function<String, Team> teamMember(@NotNull @NonNull String... name) {
        Objects.requireNonNull(name);
        return nm -> new Team(teamId.incrementAndGet(), nm, Arrays.stream(name)
                .map(User::create)
                .peek(u -> usersMap.put(u.id, u))
                .collect(toSet()));
    }

    static {
        Stream.of(
                teamMember("foo", "bar", "baz").apply("sample")
                , teamMember("qux").apply("test"))
                .forEach(t -> teamMap.put(t.id, t));
    }

    @Path("users")
    public UserResource users() {
        return new UserResource();
    }

    public static class UserResource {

        @GET
        public UsersList listUsers() {
            return new UsersList(new ArrayList<>(usersMap.values()));
        }

        @GET
        @Path("{id: [0-9]+}")
        public User findUser(@PathParam("id") Integer id) {
            return usersMap.get(id);
        }

        @POST
        public User create(@FormParam("name") String name) {
            final User user = User.create(name);
            usersMap.put(user.id, user);
            return user;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement
    public static class UsersList {
        private int count;
        private List<User> users;

        public UsersList(List<User> users) {
            this.count = users.size();
            this.users = users;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement
    public static class User {
        private int id;
        private String name;

        @NotNull
        @Contract("null -> fail")
        public static User create(@NotNull @NonNull String name) {
            return new User(userId.incrementAndGet(), name);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement
    public static class Team {
        private int id;
        private String name;
        private Set<User> member;
    }
}
