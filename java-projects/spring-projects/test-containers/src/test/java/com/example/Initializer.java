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

import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;

public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final MySQLContainer MYSQL = new MySQLContainer("mysql:5.7");

    static MySQLContainer database() {
        return MYSQL;
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        EnvironmentTestUtils.addEnvironment(
                "test-containers",
                context.getEnvironment(),
                "spring.datasource.url=" + MYSQL.getJdbcUrl() + "?useUnicode=true&connectionCollation=utf8_general_ci&characterSetResults=utf8&characterEncoding=utf-8",
                "spring.datasource.username=" + MYSQL.getUsername(),
                "spring.datasource.password=" + MYSQL.getPassword(),
                "spring.datasource.driver-class-name=" + MYSQL.getDriverClassName()
        );
    }
}
