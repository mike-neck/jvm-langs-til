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
package com.example.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@ApplicationScope
public class HashingService {

    private final MessageDigest md5 = MessageDigest.getInstance("MD5");

    public HashingService() throws NoSuchAlgorithmException {
    }

    public synchronized String toHash(String url) {
        md5.update(url.getBytes(StandardCharsets.UTF_8));
        final byte[] digest = md5.digest();
        return encode(digest);
    }

    @NotNull
    private static String encode(byte[] bytes) {
        final int hash = Base64.getUrlEncoder().encodeToString(bytes).hashCode();
        final byte[] bs = ByteBuffer.allocate(Integer.BYTES).putInt(hash).array();
        return Base64.getUrlEncoder().encodeToString(bs).replace("=", "");
    }
}
