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

import com.example.entity.Account;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    private static final Class<Account> ACCOUNT = Account.class;
    private final EntityManager em;

    @Inject
    public AccountRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Account save(Account account) {
        em.persist(account);
        em.flush();
        return account;
    }

    @Contract("null -> fail")
    @NotNull
    @Override
    public List<Account> save(@NotNull Account... accounts) {
        Objects.requireNonNull(accounts);
        return save(Arrays.asList(accounts));
    }

    @Contract("null -> fail")
    @NotNull
    @Override
    public List<Account> save(@NotNull List<Account> accounts) {
        accounts.forEach(em::persist);
        em.flush();
        return accounts;
    }

    @Override
    public List<Account> findAll() {
        return em.createQuery("select a from Account as a", Account.class)
                .getResultList();
    }

    @Override
    public Optional<Account> findById(Long id) {
        final Account account = em.find(ACCOUNT, id);
        return Optional.ofNullable(account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        final TypedQuery<Account> query = em.createQuery("select a from Account as a where a.email = :email", ACCOUNT);
        final List<Account> accounts = query.setParameter("email", email)
                .getResultList();
        return asOptional(accounts);
    }

    private static <T> Optional<T> asOptional(List<T> resultList) {
        if (resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultList.get(0));
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        final List<Account> accounts = em.createQuery("select a from Account as a where a.name = :username", ACCOUNT)
                .setParameter("username", username)
                .getResultList();
        return asOptional(accounts);
    }

    @Override
    public Account update(Account account) {
        return em.merge(account);
    }

    @Override
    public void delete(Account account) {
        em.remove(account);
    }
}
