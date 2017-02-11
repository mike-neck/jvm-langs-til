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

import com.example.entity.AccountName;
import com.example.entity.Activation;
import com.example.entity.ActivationTeam;
import com.example.entity.Privilege;
import com.example.story.Scenario;
import com.example.story.Story;
import com.example.value.single.Password;
import com.example.value.single.Username;
import com.google.inject.persist.Transactional;

public interface AccountService {

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_USER_CREATE_NEW_ACCOUNT)
    Activation createNewAccount(String email);

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_USER_MAIL_VERIFICATION)
    AccountName userEmailVerification(String token, Username username, Password password);

    @Transactional
    @Scenario(Story.TEAM_ORGANIZATION_INVITING_MEMBER)
    ActivationTeam inviteNewAccount(Long teamId, String email, Privilege... privileges);
}
