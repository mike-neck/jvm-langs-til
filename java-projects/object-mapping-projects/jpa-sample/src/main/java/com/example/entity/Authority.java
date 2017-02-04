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
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -7944403670406638030l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(name = "account_id")
            , inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Account account;

    @ManyToOne(optional = false, cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "team_id")
            , inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Privilege privilege;

    public Authority(Account account, Team team, Privilege privilege) {
        this.account = account;
        this.team = team;
        this.privilege = privilege;
    }

    @SuppressWarnings("Contract")
    @NotNull
    @Contract("null,_->fail;_,null->fail")
    public static Predicate<Authority> findByTeamAndPrivilege(
            @NotNull @NonNull Team team, @NotNull @NonNull Privilege privilege) {
        return a -> a.team.equals(team) && a.privilege.equals(privilege);
    }
}
