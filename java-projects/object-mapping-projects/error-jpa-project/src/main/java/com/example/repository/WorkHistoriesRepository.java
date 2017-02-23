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
package com.example.repository;

import com.example.entity.WorkHistories;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class WorkHistoriesRepository {

    private final EntityManager em;
    private final ZoneId zone;

    @Inject
    public WorkHistoriesRepository(EntityManager em, ZoneId zone) {
        this.em = em;
        this.zone = zone;
    }

    public WorkHistories create(WorkHistories wh) {
        wh.setCreated(LocalDateTime.now(zone));
        em.persist(wh);
        return wh;
    }

    public List<WorkHistories> listAll() {
        return em.createQuery("select wh from WorkHistories wh order by wh.created", WorkHistories.class)
                .getResultList();
    }
}
