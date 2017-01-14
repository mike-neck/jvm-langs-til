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
package com.example.sample2;

import com.example.Resource;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

public class Main {

    private static final String XML_FILE = "com/example/sample2/bool.xml";

    public static void main(String[] args) throws IOException {
        final Bool obj = new Bool(10, true);
        final StringWriter w = new StringWriter();
        JAXB.marshal(obj, w);
        System.out.println(w.toString());

        try (Reader r = Resource.getInstance().getReader(XML_FILE)) {
            final Bool bool = JAXB.unmarshal(r, Bool.class);
            System.out.println(bool);
        }
    }
}
