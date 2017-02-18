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
package com.example.repository;

import com.example.annotation.Algorithm;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DigestService {

    @Algorithm
    private final String algorithm;

    @Inject
    public DigestService(@Algorithm String algorithm) {
        this.algorithm = algorithm;
    }

    public String createId(String name, LocalDateTime created, ZoneId zone) {
        final MessageDigest md = createDigest();
        md.update(name.getBytes());
        md.update(longToBytes(created, zone));
        final byte[] digest = md.digest();
        return toHexString(digest);
    }

    @NotNull
    private String toHexString(byte[] digest) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @NotNull
    private byte[] longToBytes(LocalDateTime created, ZoneId zone) {
        final ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        final ZonedDateTime zdt = ZonedDateTime.of(created, zone);
        return buf.putLong(0, zdt.toInstant().toEpochMilli()).array();
    }

    private MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no algorithm: " + algorithm);
        }
    }
}
