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

import java.io.File;

public class Ts2Kt {

    private File sourceDir;

    private File destinationDir;

    public Ts2Kt(File sourceDir, File destinationDir) {
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ts2Kt{");
        sb.append("sourceDir=").append(sourceDir);
        sb.append(", destinationDir=").append(destinationDir);
        sb.append('}');
        return sb.toString();
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    public File getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }
}
