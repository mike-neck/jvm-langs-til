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

import com.example.entity.*;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.BadRequestException;
import com.example.exception.type.BadRequest;
import com.example.repository.AccountRepository;
import com.example.repository.ActivationRepository;
import com.example.repository.TeamRepository;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.exception.NotFoundException.notFound;

public class AccountServiceImpl implements AccountService {

    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final ActivationRepository activationRepository;
    private final HashService hashService;
    private final ZoneId zoneId;

    @Inject
    public AccountServiceImpl(TeamRepository teamRepository, AccountRepository accountRepository,
            ActivationRepository activationRepository, HashService hashService, ZoneId zoneId) {
        this.teamRepository = teamRepository;
        this.accountRepository = accountRepository;
        this.activationRepository = activationRepository;
        this.hashService = hashService;
        this.zoneId = zoneId;
    }

    @Transactional
    @Override
    public Account createNewAccount(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(a -> { throw new AccountAlreadyExistsException("createNewAccount", email); });
        final Account account = new Account(email, LocalDateTime.now(zoneId));
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public Activation inviteNewAccount(Long teamId, String email, Privilege... privileges) {
        if (privileges == null || privileges.length == 0) {
            throw new BadRequestException(AccountServiceImpl.class, "inviteNewAccount",
                    BadRequest.INVALID_NUMBER_OF_PARAMETERS,
                    "privileges");
        }
        final Team team = teamRepository.findById(teamId).orElseThrow(notFound(Team.class, teamId));
        final Set<Privilege> ps = new HashSet<>(Arrays.asList(privileges));

        final LocalDateTime now = LocalDateTime.now(zoneId);

        final Optional<Account> accOpt = accountRepository.findByEmail(email);

        final Account account = accOpt.orElseGet(() -> new Account(email, now));
        final Activation activation = new Activation(account, now.plusDays(7L),
                hashService.generateToken(teamId, email, now), now);
        final ActivationTeam activationTeam = new ActivationTeam(team, activation, ps, now);
        activationRepository.save(activation);
        return activation;
    }
}
