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
@Table(name = "production_result")
public class ProductionResult implements Serializable {

    @SuppressWarnings("LongLiteralEndingWithLowercaseL")
    private static final long serialVersionUID = -7798289353232408566l;

    @Id
    @OneToOne
    private ProductionSchedule schedule;

    @Convert(converter = LocalDateTimeConverter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "production_actual")
    private LocalDateTime productionActual;
}
