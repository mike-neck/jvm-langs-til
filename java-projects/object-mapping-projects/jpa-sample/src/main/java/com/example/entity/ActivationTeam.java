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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activation_team")
public class ActivationTeam implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -4196912682665414114l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Team team;

    @OneToOne(optional = false)
    private Activation activation;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Privilege.class)
    @Column(nullable = false, name = "privileges")
    @JoinTable(name = "activation_team_privilege"
            , joinColumns = @JoinColumn(name = "activation_id")
            , inverseJoinColumns = @JoinColumn(name = "privilege_id")

    )
    private Set<Privilege> privileges = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ActivationTeam(Team team, Activation activation, Set<Privilege> privileges, LocalDateTime createdAt) {
        this.team = team;
        this.activation = activation;
        this.privileges = privileges;
        this.createdAt = createdAt;
    }
}
