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

import com.example.entity.Account;
import com.example.entity.AccountFavoriteUrl;
import com.example.entity.SiteUrl;
import com.example.vo.UserId;
import lombok.NonNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class FavoriteUrlRepository {

    private final EntityManager em;

    @Inject
    public FavoriteUrlRepository(EntityManager em) {
        this.em = em;
    }

    List<AccountFavoriteUrl> findByUser(@NonNull final Account account) {
        return em.createQuery("select f from AccountFavoriteUrl as f where f.account = :account",
                AccountFavoriteUrl.class)
                .setParameter("account", account)
                .getResultList();
    }

    public List<AccountFavoriteUrl> findByUser(@NonNull final UserId userId) {
        return em.createQuery(
                "select f from AccountFavoriteUrl as f where f.id.accountId = :userId",
                AccountFavoriteUrl.class)
                .setParameter("userId", userId.getValue())
                .getResultList();
    }

    public AccountFavoriteUrl create(@NonNull final Account user, @NonNull final SiteUrl siteUrl) {
        final AccountFavoriteUrl fav = new AccountFavoriteUrl(user, siteUrl);
        em.persist(fav);
        return fav;
    }
}
