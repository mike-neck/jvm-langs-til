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
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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

    @ManyToOne(optional = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Privilege.class)
    @Column(nullable = false, name = "privileges")
    @JoinTable(name = "activation_privileges", joinColumns = @JoinColumn(name = "activation_id"))
    private Set<Privilege> privileges = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiration;

    @Column(length = 32)
    private String passcode;

    public Activation(Account account, Team team, Set<Privilege> privileges, LocalDateTime expiration, String passcode) {
        this.account = account;
        this.team = team;
        this.privileges = privileges;
        this.expiration = expiration;
        this.passcode = passcode;
    }

    public Set<Authority> getAuthority() {
        return privileges.stream()
                .map(p -> new Authority(account, team, p))
                .collect(toSet());
    }
}
