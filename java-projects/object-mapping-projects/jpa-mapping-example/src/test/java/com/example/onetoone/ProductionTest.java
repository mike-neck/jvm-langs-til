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
package com.example.onetoone;

import com.example.InjectorInitializer;
import com.example.entity.onetoone.ProductionResult;
import com.example.entity.onetoone.ProductionSchedule;
import com.example.repository.onetoone.ProductionRepository;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({InjectorInitializer.class})
public class ProductionTest {

    private ProductionRepository repository;

    @BeforeEach
    void setup(Injector injector) {
        this.repository = injector.getInstance(ProductionRepository.class);
    }

    @Test
    void save() {
        final ProductionSchedule schedule = repository.saveSchedule("201702180001");
        assertNotNull(schedule.getId());

        final ProductionResult result = repository.saveResult("201702180001");
        assertNotNull(result.getSchedule());
        assertEquals(schedule.getId(), result.getSchedule().getId());
    }
}
