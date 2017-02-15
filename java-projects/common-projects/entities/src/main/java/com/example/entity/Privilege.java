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
package com.example.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Privilege {

    PRIVATE_EDIT(10),
    PRIVATE_REFER(20),
    MEMBER_EDIT(30),
    MEMBER_REFER(40),
    MEMBER_CREATION(50),
    PROJECT_CREATION(60),
    TEAM_EDIT(70),
    TEAM_REFER(80),
    SYSTEM_EDIT(200),
    SYSTEM_REFER(300)

    ;

    private final int code;

    Privilege(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @NotNull
    @Contract(" -> !null")
    public static Set<Privilege> ownerPrivileges() {
        return new HashSet<>(Arrays.asList(
                PRIVATE_EDIT
                , PRIVATE_REFER
                , MEMBER_EDIT
                , MEMBER_REFER
                , MEMBER_CREATION
                , TEAM_EDIT
                , TEAM_REFER
        ));
    }

    @NotNull
    @Contract("-> !null")
    public static Set<Privilege> userPrivileges() {
        return new HashSet<>(Arrays.asList(
                PRIVATE_EDIT
                , PRIVATE_REFER
                , MEMBER_REFER
        ));
    }

    @NotNull
    @Contract("-> !null")
    public static Set<Privilege> adminPrivileges() {
        return new HashSet<>(Arrays.asList(
                PRIVATE_EDIT
                , PRIVATE_REFER
                , MEMBER_EDIT
                , MEMBER_REFER
                , MEMBER_CREATION
                , PROJECT_CREATION
                , TEAM_EDIT
                , TEAM_REFER
        ));
    }
}
