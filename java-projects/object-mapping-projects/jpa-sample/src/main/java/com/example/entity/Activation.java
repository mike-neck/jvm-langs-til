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
package com.example.entity;

import com.example.converter.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activation")
public class Activation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiration;

    @Column(length = 64)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Activation(Account account, LocalDateTime expiration, String token, LocalDateTime createdAt) {
        this.account = account;
        this.expiration = expiration;
        this.token = token;
        this.createdAt = createdAt;
    }
}
