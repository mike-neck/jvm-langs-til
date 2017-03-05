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

import java.util.*;

import static java.util.Collections.unmodifiableMap;

public enum Lang {
    JAVA   ("java-projects",    "java",     "build.gradle", true),
    GROOVY ("groovy-projects",  "groovy",   "build.gradle", true),
    KOTLIN ("kotlin-projects",  "kotlin",   "kotlin.gradle", true),
    SCALA  ("scala-projects",   "scala",    "build.gradle", false),
    FREGE  ("frege-projects",   "frege",    "build.gradle", false);

    private final String dir;
    private final String lang;
    private final String template;
    private final boolean packageRequiring;

    public String getDir() {
        return dir;
    }

    public String getLang() {
        return lang;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isPackageRequiring() {
        return packageRequiring;
    }

    Lang(String dir, String lang, String template, boolean packageRequiring) {
        this.dir = dir;
        this.lang = lang;
        this.template = template;
        this.packageRequiring = packageRequiring;
    }

    private void addTo(Map<String, Lang> map) {
        map.put(dir, this);
    }

    public static Map<String, Lang> getLangMap() {
        final Map<String, Lang> map = new HashMap<>();
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
