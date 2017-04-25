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
package com.example.repo;

import com.example.entity.SiteUrl;
import com.example.vo.SiteUrlId;
import com.example.vo.Url;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;

public class SiteUrlRepository {

    private final EntityManager em;

    @Inject
    public SiteUrlRepository(final EntityManager em) {
        this.em = em;
    }

    public Optional<SiteUrl> find(final SiteUrlId siteUrlId) {
        return Optional.ofNullable(em.find(SiteUrl.class, siteUrlId.getValue()));
    }

    public SiteUrl create(final Url url) {
        final SiteUrl siteUrl = new SiteUrl(url.getValue());
        em.persist(siteUrl);
        em.flush();
        return siteUrl;
    }
}
