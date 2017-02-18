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
package com.example.entity.onetomany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permission")
@NamedQueries({
        @NamedQuery(name = "findPermissionByUserAndFile", query =
                "select p " +
                        "from Permission p " +
                        "where p.file = :file " +
                        "and p.user = :user")
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Right> rights;

    @ManyToOne(optional = false)
    private File file;

    @ManyToOne(optional = false)
    private User user;

    public void addRight(Right right) {
        rights.add(right);
    }

    public Permission(File file, User user, Right... rights) {
        this.file = file;
        this.user = user;
        this.rights = new HashSet<>(Arrays.asList(rights));
    }
}
