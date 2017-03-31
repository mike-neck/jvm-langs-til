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

import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@Configuration
public class AuthConfig extends GlobalAuthenticationConfigurerAdapter {

    private final AccountRepository repository;

    public AuthConfig(AccountRepository repository) {
        this.repository = repository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> repository.findAccountByUsername(username)
                .map(a -> new User(a.getUsername(), a.getPassword(), Collections.singleton((GrantedAuthority) () -> "USER")))
                .orElseThrow(() -> new ResourceNotFoundException("user", username));
    }
}
