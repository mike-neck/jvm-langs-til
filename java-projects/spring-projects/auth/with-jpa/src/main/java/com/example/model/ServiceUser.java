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
package com.example.model;

import com.example.entity.Account;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;

import java.time.ZoneId;

public class ServiceUser extends User {

    @Getter
    private final Long id;

    @Getter
    private final ZoneId zoneId;

    public ServiceUser(@NotNull @NonNull Account account) {
        super(account.getUsername(), account.getPassword(), account.getAuthorities());
        this.id = account.getId();
        this.zoneId = account.getTimeZone();
    }

    public ServiceUser serviceUser() {
        return this;
    }
}