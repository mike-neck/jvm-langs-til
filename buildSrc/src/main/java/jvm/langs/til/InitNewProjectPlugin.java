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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Collections.unmodifiableMap;

public class InitNewProjectPlugin implements Plugin<Project> {

    private static final String JAVA = "java-projects";
    private static final String GROOVY = "groovy-projects";
    private static final String KOTLIN = "kotlin-projects";
    private static final String SCALA = "scala-projects";

    private static final List<String> EXCLUDE = Arrays.asList(
            "jvm-langs-til"
            , JAVA
            , GROOVY
            , KOTLIN
            , SCALA
    );

    private static final Map<String, String> LANG = unmodifiableMap(new HashMap<String, String>(){{
        put(JAVA, "java");
        put(GROOVY, "groovy");
        put(KOTLIN, "kotlin");
        put(SCALA, "scala");
    }});

    private static final Function<Project, String> PROJECT_LANGUAGE =
            ((Function<Project, String>)(Project::getName)).andThen(LANG::get);

    private static Predicate<Project> hasNoDirectory(Project sub) {
        return p -> {
            final String[] list = sub.getProjectDir()
                    .list((dir, name) -> new File(dir, name).isDirectory());
            return list == null || list.length == 0;
        };
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void apply(final Project project) {
        project.subprojects(p -> {
            final File srcDir = p.file("src");
            final String name = p.getName();
            final Optional<String> op = findLangProject(p, project)
                    .filter(hasNoDirectory(p))
                    .map(PROJECT_LANGUAGE);
            if (!srcDir.exists() && !EXCLUDE.contains(name) && op.isPresent()) {
                final Task task = p.getTasks().create("makeSrcDir");
                task.setGroup("Prepare project tasks");
                task.setDescription("Prepares src directory.");
                final String lang = op.get();
                final File mainSrc = p.file("src/main/" + lang);
                final File mainRes = p.file("src/main/resources");
                final File testSrc = p.file("src/test/" + lang);
                final File testRes = p.file("src/test/resources");
                task.doLast(t -> {
                    mainSrc.mkdirs();
                    mainRes.mkdirs();
                    testSrc.mkdirs();
                    testRes.mkdirs();
                });
            }
        });
    }

    private Optional<Project> findLangProject(final Project sub, final Project root) {
        Project current = Objects.requireNonNull(sub);
        while (!root.equals(current)) {
            final String name = current.getName();
            if (LANG.containsKey(name)) {
                return Optional.of(current);
            }
            current = current.getParent();
        }
        return Optional.empty();
    }
}
