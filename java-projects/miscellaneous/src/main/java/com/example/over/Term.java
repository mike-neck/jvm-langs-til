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
package com.example.over;

import org.jetbrains.annotations.Contract;

public enum Term {
    LATEST(1),
    TWO_MONTH(2),
    THREE_MONTH(3),
    FOUR_MONTH(4),
    FIVE_MONTH(5);

    private final int length;

    Term(int length) {
        this.length = length;
    }

    @Contract(pure = true)
    public int getSkip() {
        return 12 - length;
    }

    public int getSkip(int maxLength) {
        if (maxLength < this.length) {
            return 0;
        } else {
            return maxLength - this.length;
        }
    }

    public int getLength() {
        return length;
    }
}
