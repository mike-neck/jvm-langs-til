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
package com.example.provider;

import javax.inject.Inject;
import javax.inject.Provider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestProvider implements Provider<MessageDigest> {

    private final Algorithm algorithm;

    @Inject
    public MessageDigestProvider(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public MessageDigest get() {
        try {
            return MessageDigest.getInstance(algorithm.getName());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Invalid algorithm was set", e);
        }
    }

    public static class Algorithm {
        private final String name;
        public Algorithm(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
}
