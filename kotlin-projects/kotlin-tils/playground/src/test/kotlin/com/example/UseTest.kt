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
package com.example

import org.junit.jupiter.api.Test
import java.io.Closeable

class UseTest {

    @Test fun runTest(): Unit = CloseableImpl().use(CloseableImpl::running)

    @Test fun lambdaOfCloseable(): Unit = Closeable { println("lambda expression") }.use(::println)
}

class CloseableImpl : Closeable {

    fun running(): Unit = println("in use block")

    override fun close(): Unit = println("close")
}
