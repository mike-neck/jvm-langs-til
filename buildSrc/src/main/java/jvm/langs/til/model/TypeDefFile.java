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
package jvm.langs.til.model;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TypeDefFile {

    private final String lib;
    private final String file;
    private final Path path;
    private final Path relative;

    public TypeDefFile(Path root, Path path) {
        this.path = path;
        this.relative = root.relativize(path);
        this.lib = relative.getName(0).toString();
        this.file = relative.getFileName().toString();
    }

    public String getLib() {
        return lib;
    }

    public String getFile() {
        return file;
    }

    public Path getPath() {
        return path;
    }

    public Path getRelative() {
        return relative;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TypeDefFile{");
        sb.append("lib='").append(lib).append('\'');
        sb.append(", file='").append(file).append('\'');
        sb.append(", path=").append(path);
        sb.append(", relative=").append(relative);
        sb.append('}');
        return sb.toString();
    }

    public static class Finder extends SimpleFileVisitor<Path> {

        private final List<TypeDefFile> files;
        private final Path root;

        public Finder(Path root) {
            this.files = new ArrayList<>();
            this.root = root;
        }

        public List<TypeDefFile> findTypeFiles() throws IOException {
            Files.walkFileTree(root, this);
            return this.files;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.getFileName().toString().endsWith("d.ts")) {
                files.add(new TypeDefFile(root, file));
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
