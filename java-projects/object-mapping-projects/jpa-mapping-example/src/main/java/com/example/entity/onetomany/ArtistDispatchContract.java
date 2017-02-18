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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artist_dispatch_contract")
public class ArtistDispatchContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 32, updatable = false)
    private String customerName;

    @ManyToOne(optional = false)
    private Artist artist;

    @Convert(converter = LocalDateConverter.class)
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date;

    public ArtistDispatchContract(String customerName, Artist artist, LocalDate date) {
        this.customerName = customerName;
        this.artist = artist;
        this.date = date;
    }
}
