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
import com.example.entity.Activation;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    List<Account> save(Account... accounts);

    List<Account> save(List<Account> accounts);

    List<Account> findAll();

    Optional<Account> findById(Long id);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    Account update(Account account);

    void delete(Account account);
}