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
package com.example.beans;

import com.example.conf.PasswordConfig;
import de.mkammerer.argon2.Argon2;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class PasswordHashing {

    private final Argon2 argon2;

    private final PasswordConfig config;

    @Inject
    public PasswordHashing(Argon2 argon2, PasswordConfig config) {
        this.argon2 = argon2;
        this.config = config;
    }

    @NotNull
    @Contract("null->fail")
    public String getPasswordHash(@NotNull @NonNull String password) {
        return argon2.hash(config.iteration(), config.memory(), config.parallel(), password);
    }

    @Contract("null,_->fail;_,null->fail")
    public boolean verify(@NotNull @NonNull String hash, @NotNull @NonNull String password) {
        return argon2.verify(hash, password);
    }
}
