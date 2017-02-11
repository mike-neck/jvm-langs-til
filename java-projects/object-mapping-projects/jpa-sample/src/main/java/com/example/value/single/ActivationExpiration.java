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
package com.example.value.single;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ActivationExpiration implements Value<LocalDateTime> {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = 2943629135640822494l;

    private final LocalDateTime value;

    public static ActivationExpiration fromNow(LocalDateTime now) {
        return new ActivationExpiration(now.plusDays(7L));
    }

    @NotNull
    @Contract("null->fail;_ -> !null")
    public static ActivationExpiration fromNow(@NotNull @NonNull CreatedAt now) {
        return new ActivationExpiration(now.getValue().plusDays(7L));
    }
}
