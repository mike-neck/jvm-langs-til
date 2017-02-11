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
import com.example.value.single.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({TestInitializer.class})
public class HashServiceTest {

    @Test
    void password(HashService service) {
        final String password = "test-password";
        final String hash = service.hashPassword(new Password(password));
        assertNotEquals(password, hash);
    }

    @Test
    void verify(HashService service) {
        final String password = "test-password";
        final String hash = "$argon2i$v=19$m=8192,t=3,p=1$Aoxq7Z1VOKrtXIVKZM6SrQ$Via6nMZuAhlDWycXnMkSHYFA9Ss/4Pn5OETLb6GgLOg";
        final boolean result = service.verifyPassword(hash, new Password(password));
        assertTrue(result);
    }
}
