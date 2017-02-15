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
import com.example.entity.*;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.exception.type.BadRequest;
import com.example.repository.AccountRepository;
import com.example.repository.ActivationRepository;
import com.example.repository.TeamRepository;
import com.example.data.Tuple;
import com.example.value.single.*;
import com.google.inject.persist.Transactional;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.exception.NotFoundException.notFound;
import static java.util.stream.Collectors.toSet;

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
    public Activation createNewAccount(@NotNull @NonNull String email) {
        accountRepository.findByEmail(email)
                .ifPresent(a -> { throw new AccountAlreadyExistsException("createNewAccount", email); });
        final LocalDateTime now = LocalDateTime.now(zoneId);
        final Account account = new Account(email, now);
        accountRepository.save(account);
        final Activation activation = new Activation(account, ActivationExpiration.fromNow(now), hashService
                .generateToken(email, now), new CreatedAt(now));
        return activationRepository.save(activation);
    }

    @Transactional
    @Override
    public AccountName userEmailVerification(@NotNull @NonNull String token, @NotNull @NonNull Username username,
            @NotNull @NonNull Password password) {
        final Optional<Activation> op = activationRepository.findNotExpiredActivationByToken(token);
        final Activation activation = op.orElseThrow(() -> new NotFoundException(Activation.class, token));
        final Account account = activation.getAccount();
        final LocalDateTime now = LocalDateTime.now(zoneId);

        final AccountName name = new AccountName(account, username.getValue(), now);
        final AccountPassword pass = new AccountPassword(account, hashService.hashPassword(password), now);

        activationRepository.delete(activation);
        accountRepository.save(name, pass);
        accountRepository.save(account);
        return name;
    }

    @NotNull
    @Override
    public AccountName userLogin(@NotNull @NonNull String email, @NotNull @NonNull Password password) {
        return accountRepository.findByEmail(email)
                .map(Tuple.mkTuple(Account::getPassword))
                .map(Tuple.mapTuple(AccountPassword::getPassword))
                .filter(Tuple.conditionTuple(h -> hashService.verifyPassword(h, password)))
                .map(Tuple::getLeft)
                .map(Account::getName)
                .orElseThrow(() -> new NotFoundException(Account.class, email));
    }

    @NotNull
    @Transactional
    @Override
    public PaymentMethod createPaymentMethod(@NotNull Long accountId, @NotNull String paymentMethodName) {
        return accountRepository.findAccountById(accountId)
                .map(a -> new PaymentMethod(a, paymentMethodName, LocalDateTime.now(zoneId)))
                .map(accountRepository::save)
                .orElseThrow(notFound(Account.class, accountId));
    }

    @Transactional
    @Override
    public Team createNewTeam(
            @NotNull @NonNull AccountId aid,
            @NonNull PaymentMethodId pid,
            @NotNull @NonNull String name) {
        return ExOptional.of(accountRepository.findAccountById(aid.getValue()), notFound(Account.class, aid))
                .flatMap(a -> accountRepository.findPaymentByAccountAndId(a, pid.getValue()),
                        a -> new NotFoundException(PaymentMethod.class, pid))
                .map(Tuple.mkTuple(p -> new Team(name, p, LocalDateTime.now(zoneId))))
                .map(Tuple.mapTuple(teamRepository::save))
                .map(Tuple::reverse)
                .map(Tuple.mapTuple(PaymentMethod::getAccount))
                .map(Tuple.mkTuple(AccountServiceImpl::createOwnerAuthorities))
                .map(Tuple.mapTuple(teamRepository::save))
                .map(Tuple.bimapTuple((t, s) -> setPrivileges(t.getRight(), s)))
                .map(Tuple.mapTuple(accountRepository::save))
                .map(Tuple::getLeft)
                .map(Tuple::getLeft)
                .getOrThrow();
    }

    @NotNull
    @Contract("null->fail")
    private static Set<Authority> createOwnerAuthorities(@NotNull @NonNull Tuple<Team, Account> tuple) {
        return Privilege.ownerPrivileges()
                .stream()
                .map(p -> new Authority(tuple.getRight(), tuple.getLeft(), p))
                .collect(toSet());
    }

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    private static Account setPrivileges(@NotNull @NonNull Account account,
            @NotNull @NonNull Set<Authority> privileges) {
        account.setPrivileges(privileges);
        return account;
    }

    @NotNull
    @Transactional
    @Override
    public ActivationTeam inviteNewAccount(@NotNull @NonNull Long teamId, @NotNull @NonNull String email,
            @NotNull @NonNull Privilege... privileges) {
        if (privileges.length == 0) {
            throw new BadRequestException(AccountServiceImpl.class, "inviteNewAccount",
                    BadRequest.INVALID_NUMBER_OF_PARAMETERS,
                    "privileges");
        }
        final Team team = teamRepository.findById(teamId).orElseThrow(notFound(Team.class, teamId));
        final Set<Privilege> ps = new HashSet<>(Arrays.asList(privileges));
        final LocalDateTime now = LocalDateTime.now(zoneId);

        final Optional<Account> accOpt = accountRepository.findByEmail(email);

        final Account account = accOpt.orElseGet(() -> new Account(email, now));
        accountRepository.save(account);
        final Activation activation = new Activation(account, ActivationExpiration.fromNow(now),
                hashService.generateToken(email, now), new CreatedAt(now));
        activationRepository.save(activation);

        final ActivationTeam activationTeam = new ActivationTeam(team, activation, ps, now);
        return activationRepository.save(activationTeam);
    }
}
