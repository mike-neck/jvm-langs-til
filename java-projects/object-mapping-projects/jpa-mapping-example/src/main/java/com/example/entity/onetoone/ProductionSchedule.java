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
package com.example.entity.onetoone;

import com.github.marschall.threeten.jpa.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "production_schedule",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"productName"})}
)
@NamedQueries({
        @NamedQuery(
                name = "ProductionSchedule.findByName",
                query = "select ps from ProductionSchedule ps where ps.productName = :name")
})
public class ProductionSchedule implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -4134842560384479765l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", length = 32)
    private String productName;

    @Convert(converter = LocalDateTimeConverter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "production_expected")
    private LocalDateTime productionExpected;

    public ProductionSchedule(String productName, LocalDateTime productionExpected) {
        this.productName = productName;
        this.productionExpected = productionExpected;
    }
}
