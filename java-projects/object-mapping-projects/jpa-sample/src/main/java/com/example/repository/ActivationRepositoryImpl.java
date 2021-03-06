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

import com.example.entity.Activation;
import com.example.entity.ActivationTeam;
import com.example.value.single.AccountId;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class ActivationRepositoryImpl implements ActivationRepository {

    private final EntityManager em;
    private final ZoneId zoneId;

    @Inject
    public ActivationRepositoryImpl(EntityManager em, ZoneId zoneId) {
        this.em = em;
        this.zoneId = zoneId;
    }

    @NotNull
    @Contract("null->fail")
    @Override
    public Activation save(@NotNull @NonNull Activation activation) {
        em.persist(activation);
        return activation;
    }

    @NotNull
    @Contract("null->fail")
    @Override
    public Optional<Activation> findNotExpiredActivationById(@NotNull @NonNull Long id) {
        return em.createQuery(
                "select a from Activation a " +
                        "where a.id = :id " +
                        "and a.expiration <= :now", Activation.class)
                .setParameter("id", id)
                .setParameter("now", LocalDateTime.now(zoneId))
                .getResultList()
                .stream()
                .findFirst();
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public Optional<Activation> findNotExpiredActivationByToken(@NotNull @NonNull String token) {
        return em.createQuery(
                "select a " +
                        "from Activation a " +
                        "where a.token = :token " +
                        "and :now < a.expiration", Activation.class)
                .setParameter("token", token)
                .setParameter("now", LocalDateTime.now(zoneId))
                .getResultList()
                .stream()
                .findFirst();
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public AccountId delete(@NotNull @NonNull Activation activation) {
        final AccountId accountId = new AccountId(activation.getAccount().getId());
        em.remove(activation);
        return accountId;
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public ActivationTeam save(@NotNull @NonNull ActivationTeam activationTeam) {
        em.persist(activationTeam);
        return activationTeam;
    }
}
