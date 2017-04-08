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

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.BookmarkRepository;
import com.example.service.HashingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootApplication
public class App {

    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;
    private final HashingService hashingService;
    private final PasswordEncoder passwordEncoder;

    public App(AccountRepository accountRepository, BookmarkRepository bookmarkRepository,
            HashingService hashingService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.hashingService = hashingService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            final List<Account> accounts = Arrays.asList(
                    new Account("foo", passwordEncoder.encode("foo-password")),
                    new Account("bar", passwordEncoder.encode("bar-password")));
            accountRepository.save(accounts);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Bean
    ZoneId zoneId() {
        return ZoneId.of("Asia/Tokyo");
    }

    @Bean
    DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("uuuu/MM/dd");
    }
}
