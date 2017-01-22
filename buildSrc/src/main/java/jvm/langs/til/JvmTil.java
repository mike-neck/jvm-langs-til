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
package jvm.langs.til;

import jvm.langs.til.model.Model;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskExecutionException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Collections.unmodifiableMap;

public final class JvmTil {

    private static final JvmTil INSTANCE = new JvmTil();

    private final TemplateEngine engine;

    private final ClassLoader loader = getClass().getClassLoader();

    private JvmTil() {
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver(loader);
        resolver.setPrefix("templates/");
        resolver.setSuffix(".thymeleaf");
        resolver.setTemplateMode(TemplateMode.TEXT);
        this.engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    public void write(Task task, String view, Model model, File file) {
        final Context context = new Context(Locale.getDefault(), model.getMap());
        try (FileWriter w = new FileWriter(file)) {
            engine.process(view, context, w);
        } catch (IOException e) {
            throw new TaskExecutionException(task, e);
        }
    }

    public static void createFromTemplate(Task task, String view, Model model, File file) {
        INSTANCE.write(task, view, model, file);
    }

    public enum Lang {
        JAVA   ("java-projects", "java")
        , GROOVY ("groovy-projects", "groovy")
        , KOTLIN ("kotlin-projects", "kotlin")
        , SCALA  ("scala-projects", "scala")
        ;

        private final String dir;
        private final String lang;

        public String getDir() {
            return dir;
        }

        public String getLang() {
            return lang;
        }

        Lang(String dir, String lang) {
            this.dir = dir;
            this.lang = lang;
        }

        private void addTo(Map<String, String> map) {
            map.put(dir, lang);
        }

        static Map<String, String> getLangMap() {
            final Map<String, String> map = new HashMap<>();
            for (Lang lang : values()) {
                lang.addTo(map);
            }
            return unmodifiableMap(map);
        }

        public static List<String> getDirectories() {
            final List<String> list = new ArrayList<>();
            for (Lang lang : values()) {
                list.add(lang.dir);
            }
            return Collections.unmodifiableList(list);
        }

        public static List<String> getDirectoriesWith(String... dirs) {
            if (dirs == null) {
                return getDirectories();
            }
            final List<String> list = new ArrayList<>();
            Collections.addAll(list, dirs);
            for (Lang lang : values()) {
                list.add(lang.dir);
            }
            return Collections.unmodifiableList(list);
        }
    }

    public static final Function<Project, String> PROJECT_LANGUAGE =
            ((Function<Project, String>)(Project::getName)).andThen(k -> Lang.getLangMap().get(k));

    public static Predicate<Project> hasNoDirectory(Project sub) {
        return p -> {
            final String[] list = sub.getProjectDir()
                    .list((dir, name) -> new File(dir, name).isDirectory());
            return list == null || list.length == 0;
        };
    }

    public static Optional<Project> findLangProject(final Project sub, final Project root) {
        Project current = Objects.requireNonNull(sub);
        while (!root.equals(current)) {
            final String name = current.getName();
            if (Lang.getLangMap().containsKey(name)) {
                return Optional.of(current);
            }
            current = current.getParent();
        }
        return Optional.empty();
    }

    public static final String GROUP_NAME = "Prepare project tasks";
}
