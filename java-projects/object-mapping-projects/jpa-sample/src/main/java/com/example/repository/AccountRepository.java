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

import com.example.entity.*;
import com.example.value.single.PaymentMethodId;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    @Contract("null -> fail")
    @NotNull
    Account save(@NotNull Account account);

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    AccountName save(@NotNull AccountName name, @NotNull AccountPassword password);

    @NotNull
    @Contract("null -> fail")
    AccountName save(@NotNull AccountName name);

    @Contract("null -> fail")
    void save(@NotNull AccountPassword password);

    @Contract("null -> fail")
    @NotNull
    List<Account> save(@NotNull List<Account> accounts);

    @NotNull
    List<Account> findAll();

    @Contract("null -> fail")
    @NotNull
    Optional<Account> findAccountById(@NotNull Long id);

    @Contract("null -> fail")
    @NotNull
    Optional<Account> findByEmail(@NotNull String email);

    @Contract("null -> fail")
    @NotNull
    Optional<Account> findByUsername(@NotNull String username);

    @Contract("null -> fail")
    @NotNull
    Account update(@NotNull Account account);

    @Contract("null -> fail")
    void delete(@NotNull Account account);

    @Contract("null -> fail")
    @NotNull
    PaymentMethod save(@NotNull PaymentMethod paymentMethod);

    @Contract("null,_->fail;_,null->fail")
    @NotNull
    Optional<PaymentMethod> findPaymentByAccountAndName(@NotNull Account account, @NotNull String name);
}
