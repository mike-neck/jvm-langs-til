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
package com.example.sample4;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final Order order = new Order(
                1,
                "Java入門",
                3,
                1080,
                108,
                972 * 3);
        final StringWriter w = new StringWriter();
        JAXB.marshal(order, w);
        System.out.println(w.toString());

        final Database database = new Database(
                "jdbc:h2:mem:app-name", 
                "org.h2.Driver", 
                "user", 
                "pass",
                "h2 in-memory database");
        final StringWriter sw = new StringWriter();
        JAXB.marshal(database, sw);
        System.out.println(sw.toString());

        final Database d1 = new Database(
                "jdbc:mysql://localhost:3306",
                "com.mysql.jdbc.Driver",
                "username",
                "password",
                "database setting for production");
        final List<Database> databases = Arrays.asList(database, d1);
        final DbConnections conn = new DbConnections(databases);
        final StringWriter s = new StringWriter();
        JAXB.marshal(conn, s);
        System.out.println(s.toString());
    }
}
