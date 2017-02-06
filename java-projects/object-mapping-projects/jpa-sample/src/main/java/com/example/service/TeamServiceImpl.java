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

import com.example.entity.Account;
import com.example.entity.Authority;
import com.example.entity.Privilege;
import com.example.entity.Team;
import com.example.exception.NotFoundException;
import com.example.exception.type.BadRequest;
import com.example.repository.AccountRepository;
import com.example.repository.TeamRepository;
import com.google.inject.persist.Transactional;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.example.exception.BadRequestException.badRequest;
import static com.example.exception.NotFoundException.notFound;
import static java.util.stream.Collectors.toSet;

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

    @Transactional
    @Override
    public Team createNewTeam(String name) {
        final Team team = new Team(name, LocalDateTime.now(zoneId));
        return teamRepository.save(team);
    }

    @Transactional
    @Override
    public Account signInAsNewAccount(Long teamId, String name, String email, String password, Privilege... 
            privileges) {
        final LocalDateTime now = LocalDateTime.now(zoneId);

        final Account account = new Account(email, now);
        final Team team = teamRepository.findById(teamId)
                .orElseThrow(teamNotFound(teamId));
        final Set<Authority> authorities = Optional.ofNullable(privileges)
                .filter(as -> as.length > 0)
                .map(Arrays::stream)
                .map(s -> s.map(p -> new Authority(account, team, p)))
                .map(s -> s.collect(toSet()))
                .orElseThrow(badRequest(TeamServiceImpl.class, "signInAsNewAccount", BadRequest
                        .INVALID_NUMBER_OF_PARAMETERS, privileges));
        account.addPrivileges(authorities);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @SuppressWarnings("Contract")
    @NotNull
    @Contract("null -> fail")
    private static Supplier<NotFoundException> teamNotFound(@NotNull @NonNull Long teamId) {
        return notFound(Team.class, teamId);
    }
}
