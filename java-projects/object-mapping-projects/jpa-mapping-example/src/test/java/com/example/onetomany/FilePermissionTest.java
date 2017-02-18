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
import com.example.entity.onetomany.File;
import com.example.entity.onetomany.Permission;
import com.example.entity.onetomany.Right;
import com.example.entity.onetomany.User;
import com.example.repository.onetomany.FilePermissionRepository;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({InjectorInitializer.class})
public class FilePermissionTest {

    private FilePermissionRepository repository;

    @BeforeEach
    void setup(Injector injector) {
        this.repository = injector.getInstance(FilePermissionRepository.class);
    }

    @Test
    void testCreateUser() {
        final User user = repository.createUser("first-user");
        assertNotNull(user.getId());
        assertEquals("first-user", user.getName());
    }

    @Test
    void testCreateFile() {
        final File file = repository.createFile("test.txt");
        assertNotNull(file.getKey());
        assertEquals("test.txt", file.getName());
    }

    @Test
    void fileWithSameNameHasDifferentKey() {
        final File f1 = repository.createFile("same-name");
        final File f2 = repository.createFile("same-name");
        assertNotEquals(f1, f2);
    }

    @Test
    void defaultPermissionIsReadOnly() {
        final User user = repository.createUser("user");
        final File file = repository.createFile("file");
        final Permission permission = repository.checkFilePermission(user, file);
        assertNotNull(permission.getId());
        assertEquals(Collections.singleton(Right.READ), permission.getRights());
    }

    @Test
    void addPermission() {
        final User user = repository.createUser("user");
        final File file = repository.createFile("file");
        final Permission permission = repository.checkFilePermission(user, file);
        final Permission changed = repository.changePermission(permission, Right.READ, Right.WRITE, Right.EXECUTE);
        final Set<Right> rights = changed.getRights();
        assertEquals(3, rights.size());
        assertTrue(rights.contains(Right.READ));
        assertTrue(rights.contains(Right.WRITE));
        assertTrue(rights.contains(Right.EXECUTE));
    }
}
