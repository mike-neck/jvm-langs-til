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
package com.example.entity;

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
@Table(name = "account_favorite_url")
public class AccountFavoriteUrl implements Serializable {

    public static final long serialVersionUID = 1L;

    @EmbeddedId
    private AccountFavoriteUrlKey id;

    @ManyToOne(optional = false)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(optional = false)
    @MapsId("siteUrlId")
    @JoinColumn(name = "site_url_id")
    private SiteUrl siteUrl;

    public AccountFavoriteUrl(final Account account, final SiteUrl siteUrl) {
        this.id = new AccountFavoriteUrlKey(account.getId(), siteUrl.getId());
        this.account = account;
        this.siteUrl = siteUrl;
    }

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    @PrePersist
    public void onPersist() {
        this.created = LocalDateTime.now();
    }
}
