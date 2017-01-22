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

import jvm.langs.til.JvmTil;
import jvm.langs.til.model.Model;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;

public class MakeSrcDirTask extends DefaultTask {

    public static final String TASK_NAME = "makeSrcDir";

    public static final String GROUP_NAME = JvmTil.GROUP_NAME;

    public static final String DESCRIPTION = "Prepares src directory.";

    public static final List<String> EXCLUDE = JvmTil.Lang.getDirectoriesWith("jvm-langs-til");

    public static boolean notLangRootProject(String name) {
        return !EXCLUDE.contains(name);
    }

    private String lang;

    public void setLang(String lang) {
        this.lang = lang;
    }

    String mainSrcDir() {
        return "src/main/" + lang;
    }

    String mainResDir() {
        return "src/main/resources";
    }

    String testSrcDir() {
        return "src/test/" + lang;
    }

    String testResDir() {
        return "src/test/resources";
    }

    @TaskAction
    public void makeSrcDir() {
        final Project p = getProject();

        mkdir(p);
        writeBuildFile(p);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void mkdir(Project p) {
        p.file(mainSrcDir()).mkdirs();
        p.file(mainResDir()).mkdirs();
        p.file(testSrcDir()).mkdirs();
        p.file(testResDir()).mkdirs();
    }

    private void writeBuildFile(Project p) {
        final File buildFile = p.file("build.gradle");
        if (!buildFile.exists()) {
            final Model model = new Model();
            model.put("lang", lang);
            JvmTil.createFromTemplate(this, "build.gradle", model, buildFile);
        }
    }
}
