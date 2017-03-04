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
package jvm.langs.til.task;

import jvm.langs.til.model.Ts2Kt;
import jvm.langs.til.model.TypeDefFile;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class Ts2KtTask extends DefaultTask {

    private Ts2Kt configuration;

    public void setConfiguration(Ts2Kt configuration) {
        this.configuration = configuration;
    }

    @TaskAction
    public void action() {
        Optional.ofNullable(configuration)
                .filter(t -> t.getSourceDir().exists())
                .map(Ts2Kt::getSourceDir)
                .map(File::toPath)
                .map(TypeDefFile.Finder::new)
                .map(func(TypeDefFile.Finder::findTypeFiles))
                .ifPresent(l -> l.forEach(t -> getLogger().lifecycle(t.toString())));
    }

    @FunctionalInterface
    private interface ExFun<A, B> {
        B apply(A a) throws Exception;
    }

    private static <A, B> Function<A, B> func(ExFun<A, B> ef) {
        return a -> {
            try {
                return ef.apply(a);
            } catch (Exception e) {
                throw new GradleException("Failed to run function.", e);
            }
        };
    }
}
