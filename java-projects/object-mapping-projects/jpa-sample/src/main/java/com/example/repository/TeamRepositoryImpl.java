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

import com.example.entity.Team;
import com.example.exception.NotFoundException;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager em;

    @Inject
    public TeamRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public Team save(@NotNull @NonNull Team team) {
        em.persist(team);
        return team;
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public Optional<Team> findById(@NotNull @NonNull Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    @Override
    public Optional<Team> findByIdForUpdate(@NotNull @NonNull Long id) {
        final TypedQuery<Team> query = em.createQuery("select t from Team t where t.id = :id", Team.class);
        final Team team = query.setLockMode(LockModeType.OPTIMISTIC)
                .setParameter("id", id)
                .getSingleResult();
        return Optional.ofNullable(team);
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public Team update(@NotNull @NonNull Team team) {
        final Team t = em.find(Team.class, team.getId());
        if (!t.getId().equals(team.getId())) {
            throw new NotFoundException(Team.class, team.getId());
        }
        em.persist(team);
        return team;
    }
}
