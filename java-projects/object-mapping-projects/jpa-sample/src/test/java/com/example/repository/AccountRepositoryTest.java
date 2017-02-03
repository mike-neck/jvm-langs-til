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

import com.example.AppTest;
import com.example.TestModule;
import com.example.entity.Account;
import com.example.service.AccountService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountRepositoryTest {

    private static Injector injector;

    private AccountService service;

    @BeforeAll
    public static void prepareInjector() {
        injector = Guice.createInjector(new TestModule());
        injector.getInstance(AppTest.class);
    }

    @BeforeEach
    public void setup() {
        service = injector.getInstance(AccountService.class);
    }

    @Test
    void testSave() {
        final Account account = service.create("うさぎさん", "usagisan");
        assertNotNull(account.getId());
        final Optional<Account> found = service.findById(account.getId());
        assertTrue(found.isPresent());
        found.ifPresent(a -> assertEquals(account, a));
        if (!found.isPresent()) {
            fail("expected Account can be found by id[" + account.getId() + "], but not found.");
        }
    }
}
