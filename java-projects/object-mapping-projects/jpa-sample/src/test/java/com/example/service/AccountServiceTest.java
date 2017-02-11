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

import com.example.TestInitializer;
import com.example.entity.Account;
import com.example.entity.AccountName;
import com.example.entity.Activation;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.NotFoundException;
import com.example.story.Scenario;
import com.example.story.Story;
import com.example.value.single.Password;
import com.example.value.single.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({TestInitializer.class})
public class AccountServiceTest {

    @Scenario(Story.TEAM_ORGANIZATION_USER_CREATE_NEW_ACCOUNT)
    @Nested
    class CreateNewAccountTest {

        private static final String EXISTING_MAIL = "existing@localhost";

        @BeforeEach
        void existingAccount(AccountService service) {
            service.createNewAccount(EXISTING_MAIL);
        }

        @Test
        void creatingNewAccountWillBeSucceeded(AccountService service) {
            final Activation activation = service.createNewAccount("test01@localhost");
            assertNotNull(activation);
            final String email = activation.getAccount().getEmail();
            assertEquals("test01@localhost", email);
        }

        @Test
        void creatingNewAccountWillFailWhenMailIsExisting(AccountService service) {
            try {
                service.createNewAccount(EXISTING_MAIL);
                fail("If email is existing, account registration should fail.");
            } catch (AccountAlreadyExistsException e) {
                assertEquals("createNewAccount", e.getProcess());
            }
        }
    }

    @Scenario(Story.TEAM_ORGANIZATION_USER_MAIL_VERIFICATION)
    @Nested
    class UserEmailVerificationTest {

        private static final String NON_EXISTING_TOKEN = "xxxxxxxxxxxxxxx";

        private Activation activation;

        private final Username username = new Username("test-user");
        private final Password password = new Password("test-password");

        @BeforeEach
        void setup(AccountService service) {
            activation = service.createNewAccount("test@example.com");
        }

        @DisplayName("トークンが正しい場合は登録される")
        @Test
        void providedProperToken(AccountService service) {
            final AccountName name = service.userEmailVerification(activation.getToken(), username, password);

            assertEquals("test-user", name.getName());
        }

        @DisplayName("トークンがない場合はNotFoundException")
        @Test
        void providedNotProperToken(AccountService service) {
            try {
                service.userEmailVerification(NON_EXISTING_TOKEN, username, password);
            } catch (NotFoundException e) {
                assertTrue(e.getEntityClass().equals(Activation.class));
            }
        }
    }

    @Scenario(Story.TEAM_ORGANIZATION_USER_LOGIN)
    @Nested
    class UserLoginTest {

        private static final String MAIL = "test@example.com";
        private final Username username = new Username("test-name");
        private final Password password = new Password("test-password");

        private AccountName name;

        @BeforeEach
        void setup(AccountService service) {
            final Activation activation = service.createNewAccount(MAIL);
            name = service.userEmailVerification(activation.getToken(), username, password);
        }

        @Test
        void userCanLoginWithEmailAndPassword(AccountService service) {
            final AccountName name = service.userLogin(MAIL, password);
            assertEquals(this.name, name);
        }

        @Test
        void userCannotLoginWithUnknownEmail(AccountService service) {
            try {
                service.userLogin("unknown@example.com", password);
                fail("Unknown email is forbidden.");
            } catch (NotFoundException e) {
                assertEquals(Account.class, e.getEntityClass());
            }
        }

        @Test
        void userCannotLoginWithUnknownPassword(AccountService service) {
            try {
                service.userLogin(MAIL, new Password("test"));
                fail("Unknown email is forbidden.");
            } catch (NotFoundException e) {
                assertEquals(Account.class, e.getEntityClass());
            }
        }
    }
}
