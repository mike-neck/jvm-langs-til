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

import com.example.AppTest;
import com.example.module.TestModule;
import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.entity.Privilege.PRIVATE_EDIT;
import static com.example.entity.Privilege.PRIVATE_REFER;
import static org.junit.jupiter.api.Assertions.*;

public class TeamServiceTest {

    private static Injector injector;

    private TeamService service;
    private Long teamId;

    @BeforeAll
    public static void prepareInjector() {
        injector = Guice.createInjector(new TestModule());
        injector.getInstance(AppTest.class);
    }

    @BeforeEach
    public void setup() {
        service = injector.getInstance(TeamService.class);
        teamId = service.createNewTeam("名古屋").getId();
    }

    @Test
    void testSave() {
        final Account account = service.signInAsNewAccount(teamId, "うさぎさん", "test1@example.com", "usagisan", 
                PRIVATE_REFER, PRIVATE_EDIT);
        assertNotNull(account.getId());
        final Optional<Account> found = service.findAccountById(account.getId());
        assertTrue(found.isPresent());
        found.ifPresent(a -> assertEquals(account, a));
        if (!found.isPresent()) {
            fail("expected Account can be found by id[" + account.getId() + "], but not found.");
        }
    }

    @Test
    void testSavingNewAccountWithoutPrivilegesMakesException() {
        assertThrows(BadRequestException.class, () ->
                service.signInAsNewAccount(teamId, "新しいアカウント", "test1@example.com", "password"));
    }
}
