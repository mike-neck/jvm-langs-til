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

import jvm.langs.til.model.Ts2Kt;
import jvm.langs.til.task.Ts2KtTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class Ts2KtSupportPlugin implements Plugin<Project> {

    public static final String PLUGIN_NAME = "ts2kt-support";

    @Override
    public void apply(final Project project) {
        project.getExtensions().create("ts2kt", Ts2Kt.class,
                project.file("node_modules"),
                project.file("src/main/generated"));
        project.getTasks().create("ts2kt", Ts2KtTask.class, task -> {
            final Ts2Kt conf = project.getExtensions().getByType(Ts2Kt.class);
            task.setConfiguration(conf);
        });
    }
}
