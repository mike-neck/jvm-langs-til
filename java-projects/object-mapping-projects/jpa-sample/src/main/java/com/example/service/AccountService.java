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
import com.example.story.Scenario;
import com.example.story.Story;
import com.example.value.single.Password;
import com.example.value.single.Username;
import com.google.inject.persist.Transactional;
import org.jetbrains.annotations.NotNull;

public interface AccountService {

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_USER_CREATE_NEW_ACCOUNT)
    Activation createNewAccount(@NotNull String email);

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_USER_MAIL_VERIFICATION)
    AccountName userEmailVerification(@NotNull String token, @NotNull Username username, @NotNull Password password);

    @Scenario(Story.TEAM_ORGANIZATION_USER_LOGIN)
    AccountName userLogin(@NotNull String email, @NotNull Password password);

    @Scenario(Story.TEAM_ORGANIZATION_CREATE_PAYMENT_METHOD)
    PaymentMethod createPaymentMethod(@NotNull Long accountId, @NotNull String paymentMethodName);

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_INVITING_MEMBER)
    ActivationTeam inviteNewAccount(@NotNull Long teamId, @NotNull String email, @NotNull Privilege... privileges);
}
