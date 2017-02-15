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
import com.example.entity.AccountName;
import com.example.entity.AccountPassword;
import com.example.entity.PaymentMethod;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    private static final Class<Account> ACCOUNT = Account.class;
    private final EntityManager em;

    @Inject
    public AccountRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @NotNull
    @Override
    public Account save(@NotNull Account account) {
        em.persist(account);
        em.flush();
        return account;
    }

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    @Override
    public AccountName save(@NotNull @NonNull AccountName name, @NotNull @NonNull AccountPassword password) {
        em.persist(name);
        em.persist(password);
        em.flush();
        return name;
    }

    @NotNull
    @Contract("null -> fail")
    @Override
    public AccountName save(@NotNull @NonNull AccountName name) {
        em.persist(name);
        em.flush();
        return name;
    }

    @Contract("null -> fail")
    @Override
    public void save(@NotNull @NonNull AccountPassword password) {
        em.persist(password);
        em.flush();
    }

    @Contract("null -> fail")
    @NotNull
    @Override
    public List<Account> save(@NotNull List<Account> accounts) {
        accounts.forEach(em::persist);
        em.flush();
        return accounts;
    }

    @NotNull
    @Override
    public List<Account> findAll() {
        return em.createQuery("select a from Account as a", Account.class)
                .getResultList();
    }

    @NotNull
    @Override
    public Optional<Account> findAccountById(@NotNull Long id) {
        final Account account = em.find(ACCOUNT, id);
        return Optional.ofNullable(account);
    }

    @NotNull
    @Override
    public Optional<Account> findByEmail(@NotNull String email) {
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

    @NotNull
    @Override
    public Optional<Account> findByUsername(@NotNull String username) {
        final List<Account> accounts = em.createQuery("select a from Account as a where a.name = :username", ACCOUNT)
                .setParameter("username", username)
                .getResultList();
        return asOptional(accounts);
    }

    @NotNull
    @Override
    public Account update(@NotNull Account account) {
        return em.merge(account);
    }

    @Override
    public void delete(@NotNull Account account) {
        em.remove(account);
    }

    @NotNull
    @Override
    public PaymentMethod save(@NotNull PaymentMethod paymentMethod) {
        em.persist(paymentMethod);
        em.flush();
        return paymentMethod;
    }

    @Contract("null,_->fail;_,null->fail")
    @Override
    public Optional<PaymentMethod> findPaymentByAccountAndId(
            @NotNull @NonNull Account account,
            @NotNull @NonNull Long pid) {
        return em.createQuery(
                "select p from PaymentMethod  p " +
                        "where p.account = :account " +
                        "and p.id = :pid",
                PaymentMethod.class)
                .setParameter("account", account)
                .setParameter("pid", pid)
                .getResultList()
                .stream()
                .findFirst();
    }
}
