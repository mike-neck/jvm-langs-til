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
package com.example.entity.onetomany;

import com.github.marschall.threeten.jpa.LocalDateConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"contracts"})
@EqualsAndHashCode(exclude = {"contracts"})
@Entity
@Table(name = "artist")
@NamedQueries({
        @NamedQuery(name = "findArtistByName", query = "select a from Artist a where a.name = :name")
        , @NamedQuery(name = "findArtistById", query = "select a from Artist a where a.id = :id")
})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 64, updatable = false)
    private String name;

    @OneToMany(mappedBy = "artist")
    private Set<ArtistDispatchContract> contracts;

    @Convert(converter = LocalDateConverter.class)
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "available")
    private LocalDate available;

    public Artist(String name, LocalDate available) {
        this.name = name;
        this.contracts = new HashSet<>();
        this.available = available;
    }

    public boolean addContract(ArtistDispatchContract contract) {
        return contracts.add(contract);
    }
}
