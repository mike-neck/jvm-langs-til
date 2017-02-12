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

public enum Privilege {

    PRIVATE_EDIT    ( 10)
    , PRIVATE_REFER ( 20)

    , MEMBER_EDIT   ( 30)
    , MEMBER_REFER  ( 40)

    , TEAM_EDIT     ( 50)
    , TEAM_REFER    ( 60)

    , SYSTEM_EDIT   (200)
    , SYSTEM_REFER  (300)

    ;

    private final int code;

    Privilege(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
