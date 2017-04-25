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
package com.example;

import com.example.entity.Account;
import com.example.entity.AccountFavoriteUrl;
import com.example.entity.SiteUrl;
import com.example.repo.FavoriteUrlRepository;
import com.example.repo.UserRepository;
import com.example.vo.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({JpaExtension.class})
class UserErrorServiceTest {

    private List<AccountFavoriteUrl> favoriteUrls;
    private UserId userId;

    @BeforeEach
    void setup(@InjectBean final WithTransaction withTransaction) {
        final Account account = withTransaction.call(em -> {
            final Account ac = new Account("test user");
            em.persist(ac);
            return ac;
        });
        favoriteUrls = withTransaction.call(em ->
                Stream.of("https://google.co.jp", "https://twitter.com")
                        .map(SiteUrl::new)
                        .peek(em::persist)
                        .peek(ignore -> em.flush())
                        .map(s -> new AccountFavoriteUrl(account, s))
                        .peek(em::persist)
                        .collect(toList()));
        userId = new UserId(account.getId());
    }

    @Test
    void test(@InjectBean final EntityManager em, @InjectBean final WithTransaction withTransaction) {
        withTransaction.refreshRun(ignore -> {
            final UserRepository ur = new UserRepository(em);
            final Optional<Account> account = ur.findUserForUpdate(userId);
            assertTrue(account.isPresent());
            account.ifPresent(a -> {
                a.setUsername("test user - 2");
                ur.updateUser(a);
            });
        });

        final FavoriteUrlRepository fur = new FavoriteUrlRepository(em);
        withTransaction.run(ignore -> {
            final List<AccountFavoriteUrl> urls = fur.findByUser(userId);
            assertEquals(2, urls.size());
        });
    }
}
