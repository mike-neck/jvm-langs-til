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
package com.example.sample5;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        final Database d1 = new Database(
                DbDialect.MYSQL
                , "com.mysql.jdbc.Driver"
                , "jdbc:mysql://mysql-instance/app"
                , "user"
                , "password"
                , "main db node."
        );
        System.out.println(marshal(d1));

        final Database d2 = new Database(
                DbDialect.AURORA
                , "org.mariadb.jdbc.Driver"
                , "jdbc:mariadb://mariadb-instance/app"
                , "user"
                , "password"
                , "sub node."
        );
        System.out.println(marshal(d2));

        final DbConnections connections = new DbConnections(Arrays.asList(d1, d2));
        System.out.println(marshal(connections));
    }

    @NotNull
    @Contract(value = "_ -> !null", pure = true)
    private static <T> String marshal(@NotNull @NonNull T obj) {
        final StringWriter w = new StringWriter();
        JAXB.marshal(obj, w);
        return w.toString();
    }
}
