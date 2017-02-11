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
package com.example.service;

import com.example.data.ExOptional;
import com.example.entity.Account;
import com.example.entity.PaymentMethod;
import com.example.entity.Team;
import com.example.exception.NotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.TeamRepository;
import com.example.value.single.AccountId;
import com.example.value.single.PaymentMethodName;
import com.google.inject.persist.Transactional;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Supplier;

import static com.example.exception.NotFoundException.notFound;

public class TeamServiceImpl implements TeamService {

    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final ZoneId zoneId;

    @Inject
    public TeamServiceImpl(AccountRepository accountRepository, TeamRepository teamRepository, ZoneId zoneId) {
        this.accountRepository = accountRepository;
        this.teamRepository = teamRepository;
        this.zoneId = zoneId;
    }

    @NotNull
    @Transactional
    @Override
    public Team createNewTeam(
            @NotNull @NonNull AccountId aid,
            @NotNull @NonNull PaymentMethodName payment,
            @NotNull @NonNull String name) {
        return ExOptional.of(accountRepository.findAccountById(aid.getValue()), notFound(Account.class, aid))
                .flatMap(a -> accountRepository.findPaymentByAccountAndName(a, payment.getValue()),
                        a -> new NotFoundException(PaymentMethod.class, payment))
                .map(p -> new Team(name, p, LocalDateTime.now(zoneId)))
                .map(teamRepository::save)
                .getOrThrow();
    }

    @SuppressWarnings("Contract")
    @NotNull
    @Contract("null -> fail")
    private static Supplier<NotFoundException> teamNotFound(@NotNull @NonNull Long teamId) {
        return notFound(Team.class, teamId);
    }
}
