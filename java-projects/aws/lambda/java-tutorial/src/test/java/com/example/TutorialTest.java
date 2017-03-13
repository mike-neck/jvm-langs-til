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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TutorialTest {

    @Test
    void test() {
        final Input input = new Input("Asia/Tokyo", 1L);
        final Output output = new Tutorial().handleRequest(input, null);
        assertAll(() -> assertTrue(output.getValue() < 20),
                () -> assertTrue(output.getValue() >= 0),
                () -> assertTrue(output.getTime().contains("T")),
                () -> assertTrue(output.getTime().contains("-")),
                () -> assertTrue(output.getTime().contains(":")));
    }

    @Test
    void ifZoneIsUnknown() {
        final Input input = new Input("PPAP", 1L);
        final Output output = new Tutorial().handleRequest(input, null);
        assertNotNull(output);
    }

    @Test
    void ifSeedIsNull() {
        final Input input = new Input("Asia/Tokyo", null);
        final Output output = new Tutorial().handleRequest(input, null);
        assertNotNull(output);
    }
}
