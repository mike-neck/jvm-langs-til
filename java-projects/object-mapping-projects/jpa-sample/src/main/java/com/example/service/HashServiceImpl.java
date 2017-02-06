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

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class HashServiceImpl implements HashService {

    private final Provider<MessageDigest> provider;
    private final ZoneId zoneId;

    @Inject
    public HashServiceImpl(Provider<MessageDigest> provider, ZoneId zoneId) {
        this.provider = provider;
        this.zoneId = zoneId;
    }

    @Override
    public String generatePasscode(@NotNull @NonNull Long teamId, @NotNull @NonNull String email,
            @NotNull @NonNull LocalDateTime time) {
        final MessageDigest md = provider.get();
        md.update(longToBytes(teamId));
        md.update(email.getBytes());
        final ZoneOffset offset = zoneId.getRules().getOffset(time);
        md.update(longToBytes(time.toEpochSecond(offset)));
        return bytesToString(md.digest());
    }

    @NotNull
    private byte[] longToBytes(long value) {
        final ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.putLong(value);
        return buf.array();
    }

    private String bytesToString(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
