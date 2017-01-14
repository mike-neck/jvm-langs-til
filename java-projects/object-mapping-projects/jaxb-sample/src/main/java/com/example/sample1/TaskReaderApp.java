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
package com.example.sample1;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class TaskReaderApp {

    private static final String XML_FILE = "com/example/sample1/task.xml";

    private static final ClassLoader loader = TaskReaderApp.class.getClassLoader();

    public static void main(String[] args) throws IOException {
        final Task obj = new Task(1L, "jaxbサンプルアプリ作成", "jaxbのサンプルアプリを作る\nそしてmarshalする");
        final StringWriter w = new StringWriter();
        JAXB.marshal(obj, w);
        System.out.println(w.toString());

        try (InputStream is = loader.getResourceAsStream(XML_FILE)) {
            final Task task = JAXB.unmarshal(is, Task.class);
            System.out.println(task);
        }
    }
}
