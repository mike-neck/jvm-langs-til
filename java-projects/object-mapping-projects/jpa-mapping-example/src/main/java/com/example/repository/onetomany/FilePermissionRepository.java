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

import com.example.data.First;
import com.example.entity.onetomany.File;
import com.example.entity.onetomany.Permission;
import com.example.entity.onetomany.Right;
import com.example.entity.onetomany.User;
import com.example.repository.DigestService;
import com.google.inject.persist.Transactional;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Transactional
public class FilePermissionRepository {

    private final EntityManager em;
    private final ZoneId zone;
    private final DigestService digest;

    @Inject
    public FilePermissionRepository(EntityManager em, ZoneId zone, DigestService digest) {
        this.em = em;
        this.zone = zone;
        this.digest = digest;
    }

    public User createUser(String name) {
        final User user = new User(name);
        em.persist(user);
        em.flush();
        return user;
    }

    public File createFile(String name) {
        final LocalDateTime now = LocalDateTime.now(zone);
        final String key = digest.createId(name, now, zone);
        final File file = new File(key, name, now);
        em.persist(file);
        em.flush();
        return file;
    }

    public Permission checkFilePermission(User user, File file) {
        return First.of(findPermission(user, file))
                .orElse(() -> newPermission(user, file));
    }

    private Optional<Permission> findPermission(User user, File file) {
        return em.createNamedQuery("findPermissionByUserAndFile", Permission.class)
                .setParameter("file", file)
                .setParameter("user", user)
                .getResultList()
                .stream()
                .findFirst();
    }

    @NotNull
    private Permission newPermission(User user, File file) {
        final Permission permission = new Permission(file, user, Right.READ);
        em.persist(permission);
        em.flush();
        return permission;
    }

    public Permission changePermission(Permission permission, Right... rights) {
        final HashSet<Right> set = new HashSet<>(Arrays.asList(rights));
        permission.setRights(set);
        em.persist(permission);
        em.flush();
        return permission;
    }
}
