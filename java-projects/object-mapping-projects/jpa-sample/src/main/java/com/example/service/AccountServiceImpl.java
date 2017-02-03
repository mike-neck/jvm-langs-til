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
import com.example.repository.AccountRepository;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private static final ZoneId ASIA_TOKYO = ZoneId.of("Asia/Tokyo");

    private final AccountRepository repo;

    @Inject
    public AccountServiceImpl(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Account create(String name, String password) {
        final LocalDateTime now = LocalDateTime.now(ASIA_TOKYO);
        final Account account = new Account(name, password, now);
        return repo.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repo.findById(id);
    }
}
