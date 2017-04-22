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
package com.example;

import com.example.entity.Account;
import com.example.vo.UserId;
import com.example.vo.Username;
import lombok.NonNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.Optional;

public class UserRepository {

    private final EntityManager em;

    @Inject
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public Account createUser(@NonNull final Username username) {
        final Account account = new Account(username.getValue());
        em.persist(account);
        em.flush();
        return account;
    }

    public Optional<Account> findUser(final UserId userId) {
        return Optional.ofNullable(em.find(Account.class, userId.getValue()));
    }

    public Optional<Account> findUserForUpdate(final UserId userId) {
        return Optional.ofNullable(em.find(Account.class, userId.getValue(), LockModeType.OPTIMISTIC_FORCE_INCREMENT));
    }

    public Account updateUser(final Account account) {
        em.persist(account);
        return account;
    }
}
