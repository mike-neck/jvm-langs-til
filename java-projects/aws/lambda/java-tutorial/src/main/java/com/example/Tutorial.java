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
package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.Random;

public class Tutorial implements RequestHandler<Input, Output> {


    @Override
    public Output handleRequest(Input input, Context context) {
        final ZoneId zoneId = getZoneId(input);
        final ZonedDateTime now = ZonedDateTime.now(zoneId);
        final int value = new Random(getSeed(input)).nextInt(20);
        return new Output(now, value);
    }

    @NotNull
    private Long getSeed(Input input) {
        final Long seed = input.getSeed();
        return seed == null ? 0 : seed;
    }

    private ZoneId getZoneId(Input input) {
        try {
            return ZoneId.of(input.getZoneId());
        } catch (ZoneRulesException e) {
            return ZoneId.systemDefault();
        }
    }
}
