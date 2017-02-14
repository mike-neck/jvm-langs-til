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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "authority"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {
        "account_id", "team_id", "privilege"
})})
public class Authority implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -7944403670406638030l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account account;

    @ManyToOne(optional = false, cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Privilege privilege;

    public Authority(Account account, Team team, Privilege privilege) {
        this.account = account;
        this.team = team;
        this.privilege = privilege;
    }
}
