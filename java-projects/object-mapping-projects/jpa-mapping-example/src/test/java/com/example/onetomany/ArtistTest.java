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
package com.example.onetomany;

import com.example.InjectorInitializer;
import com.example.entity.onetomany.Artist;
import com.example.entity.onetomany.ArtistDispatchContract;
import com.example.repository.onetomany.ArtistRepository;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({InjectorInitializer.class})
public class ArtistTest {

    private ArtistRepository repository;
    private ZoneId zone;

    @BeforeEach
    void setup(Injector injector) {
        this.repository = injector.getInstance(ArtistRepository.class);
        this.zone = injector.getInstance(ZoneId.class);
    }

    @Test
    void save() {
        final Artist artist = repository.createArtist("artist-name");
        assertNotNull(artist);
        assertNotNull(artist.getId());
        assertTrue(artist.getContracts().isEmpty());
    }

    @Test
    void saveContract() {
        final Artist artist = repository.createArtist("Famous Artist");
        final LocalDate today = LocalDate.now(zone);
        final Artist created = repository.createContract(artist.getId(), "West Park", today);
        assertEquals(created, artist);
        assertTrue(created.getContracts().size() == 1);
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final ArtistDispatchContract contract = created.getContracts().stream().findAny().get();
        assertEquals("West Park", contract.getCustomerName());
    }
}
