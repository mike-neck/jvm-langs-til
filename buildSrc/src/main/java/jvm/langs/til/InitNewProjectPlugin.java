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

import jvm.langs.til.task.MakeSrcDirTask;
import jvm.langs.til.util.Pair;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Optional;

public class InitNewProjectPlugin implements Plugin<Project> {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void apply(final Project project) {
        project.subprojects(p -> Optional.ofNullable(p.file("src"))
                .filter(s -> !s.exists())
                .map(s -> p.getName())
                .filter(MakeSrcDirTask::notLangRootProject)
                .flatMap(n -> JvmTil.findLangProject(p, project))
                .filter(JvmTil.hasNoDirectory(p))
                .map(JvmTil.PROJECT_LANGUAGE)
                .map(Pair.makePair(s -> p.getTasks().create(MakeSrcDirTask.TASK_NAME, MakeSrcDirTask.class)))
                .map(Pair.rightConsume(t -> t.setGroup(MakeSrcDirTask.GROUP_NAME)))
                .map(Pair.rightConsume(t -> t.setDescription(MakeSrcDirTask.DESCRIPTION)))
                .map(Pair.biConsumePair((s, t) -> t.setLang(s)))
                .isPresent());
    }
}
