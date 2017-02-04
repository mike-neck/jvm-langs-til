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
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "privileges" })
@ToString(exclude = { "privileges" })
@Entity
@Table(name = "account")
public class Account implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -3655881587571904884l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, length = 511)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authority> privileges = new HashSet<>();

    public Account(String name, String password, LocalDateTime createdAt) {
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
    }

    public void addPrivilege(Team team, Privilege p) {
        addPrivilege(new Authority(this, team, p));
    }

    public void addPrivilege(Authority authority) {
        privileges.add(authority);
    }

    public void addPrivileges(Authority... authorities) {
        if (authorities != null) {
            addPrivileges(Arrays.asList(authorities));
        }
    }

    public void addPrivileges(Collection<Authority> authorities) {
        privileges.addAll(authorities);
    }
}
