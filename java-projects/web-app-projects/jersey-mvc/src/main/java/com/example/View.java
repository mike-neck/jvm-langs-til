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
package com.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.server.mvc.Viewable;

public class View extends Viewable {

    View(Name name) {
        super(name.value);
    }

    View(Name name, Object model) {
        super(name.value, model);
    }

    @Override
    public String getTemplateName() {
        final String name = super.getTemplateName();
        return name.startsWith("/") ? name : "/" + name;
    }

    @Override
    public boolean isTemplateNameAbsolute() {
        return true;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Name {
        private final String value;
        public View view() {
            return new View(this);
        }
        public View with(Object model) {
            return new View(this, model);
        }
    }

    public static final class Of {
        private Of() {}

        public static final Name INDEX = new Name("index");
    }
}
