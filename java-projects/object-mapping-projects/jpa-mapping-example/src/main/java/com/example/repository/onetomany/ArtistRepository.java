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
package com.example.repository.onetomany;

import com.example.data.ExOptional;
import com.example.data.Tuple;
import com.example.entity.onetomany.Artist;
import com.example.entity.onetomany.ArtistDispatchContract;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class ArtistRepository {

    private final EntityManager em;
    private final ZoneId zone;

    @Inject
    public ArtistRepository(EntityManager em, ZoneId zone) {
        this.em = em;
        this.zone = zone;
    }

    @Transactional
    public Artist createArtist(String name) {
        final Optional<Artist> existing = em.createNamedQuery("findArtistByName", Artist.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
        if (existing.isPresent()) {
            throw new IllegalStateException(String.format("artist[%s] is already existing.", name));
        }
        final Artist artist = new Artist(name, LocalDate.now(zone));
        em.persist(artist);
        em.flush();
        return artist;
    }

    @Transactional
    public Artist createContract(Long artistId, String customerName, LocalDate date) {
        return ExOptional.of(Optional.ofNullable(em.find(Artist.class, artistId)),
                () -> new IllegalArgumentException(String.format("artist[%d] is not existing.", artistId)))
                .map(Tuple.mkTuple(a -> new ArtistDispatchContract(customerName, a, date)))
                .map(Tuple.peekTuple(t -> t.getLeft().addContract(t.getRight())))
                .map(Tuple.peekTuple(t -> em.persist(t.getRight())))
                .map(Tuple.peekTuple(t -> em.persist(t.getLeft())))
                .map(Tuple::getLeft)
                .getOrThrow();
    }
}
