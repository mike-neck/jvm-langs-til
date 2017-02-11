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
import com.example.entity.Activation;
import com.example.entity.PaymentMethod;
import com.example.service.AccountService;
import com.example.value.single.Password;
import com.example.value.single.Username;

import javax.inject.Inject;

public class TestHelper {

    private final AccountService accountService;

    @Inject
    public TestHelper(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account createAccount(String email, String username, String password) {
        return createAccount(email, new Username(username), new Password(password));
    }

    public Account createAccount(String email, Username username, Password password) {
        final Activation activation = accountService.createNewAccount(email);
        return accountService.userEmailVerification(activation.getToken(), username, password)
                .getAccount();
    }

    public PaymentMethod createPayment(Account account, String methodName) {
        return accountService.createPaymentMethod(account.getId(), methodName);
    }
}
