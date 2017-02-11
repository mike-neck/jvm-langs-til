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

import com.example.TestHelper;
import com.example.TestInitializer;
import com.example.entity.Account;
import com.example.entity.PaymentMethod;
import com.example.entity.Team;
import com.example.exception.NotFoundException;
import com.example.story.Scenario;
import com.example.story.Story;
import com.example.value.single.AccountId;
import com.example.value.single.PaymentMethodName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({TestInitializer.class})
public class TeamServiceTest {

    @Scenario(Story.TEAM_ORGANIZATION_TEAM_CREATION)
    @Nested
    class TeamCreationTest {

        private Account account;
        private PaymentMethod payment;
        private Account noPayment;

        @BeforeEach
        void setup(TestHelper helper) {
            this.account = helper.createAccount("test@example.com", "test-user", "test-password");
            this.payment = helper.createPayment(account, "test-payment");
            this.noPayment = helper.createAccount("no-payment@example.com", "no-payment", "no-payment");
        }

        @Test
        void accountWithPaymentCanCreateTeam(TeamService service) {
            final Team team = service.createNewTeam(new AccountId(account.getId()),
                    new PaymentMethodName(payment.getName()),
                    "test-team");
            assertNotNull(team);
        }

        @Test
        void accountWithoutPaymentCannotCreateTeam(TeamService service) {
            try {
                service.createNewTeam(new AccountId(account.getId()), new PaymentMethodName("test"), "cannot-create");
                fail("account without payment cannot create team.");
            } catch (Exception e) {
                assertTrue(e instanceof NotFoundException);
            }
        }
    }
}
