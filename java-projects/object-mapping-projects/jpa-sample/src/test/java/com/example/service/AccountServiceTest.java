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
import com.example.exception.AccountAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({TestInitializer.class})
public class AccountServiceTest {

    @Nested
    class CreateNewAccountTest {

        private static final String EXISTING_MAIL = "existing@localhost";

        @BeforeEach
        void existingAccount(AccountService service) {
            service.createNewAccount(EXISTING_MAIL);
        }

        @Test
        void creatingNewAccountWillBeSucceeded(AccountService service) {
            final Account account = service.createNewAccount("test01@localhost");
            assertNotNull(account);
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
}
