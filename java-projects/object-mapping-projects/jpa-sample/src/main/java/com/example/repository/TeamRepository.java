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
package com.example.repository;

import com.example.entity.Authority;
import com.example.entity.Team;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface TeamRepository {

    Team save(Team team);

    @NotNull
    @Contract("null -> fail")
    Set<Authority> save(Set<Authority> authorities);

    Optional<Team> findById(Long id);

    Optional<Team> findByIdForUpdate(@NotNull @NonNull Long id);

    Team update(Team team);
}
