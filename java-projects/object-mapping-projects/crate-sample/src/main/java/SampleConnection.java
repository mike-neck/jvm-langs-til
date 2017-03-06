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

import java.sql.*;
import java.util.Properties;

public class SampleConnection {

    public static void main(String[] args) {
        try (final Connection connection = DriverManager.getConnection("crate://localhost:5432", new Properties())) {
            final Statement stmt = connection.createStatement();
            final ResultSet set = stmt.executeQuery("SELECT  " +
                    "s.last_name, " +
                    "s.first_name, " +
                    "b.title, " +
                    "date_format('%Y-%m-%dT%H:%i:%s.%fZ', r.period ['since']) AS since, " +
                    "date_format('%Y-%m-%dT%H:%i:%s.%fZ', r.period ['returned']) AS returned " +
                    "FROM student s " +
                    "JOIN renral r ON s.id = r.student_id " +
                    "JOIN books b ON r.book_id = b.id");
            while (set.next()) {
                final String first = String.format(
                        "last_name: %s, first_name: %s, title: %s",
                        set.getString("last_name"),
                        set.getString("first_name"),
                        set.getString("title"));
                final String second = String.format(
                        "since: %s, returned %s",
                        set.getString("since"),
                        set.getString("returned"));
                System.out.println(first);
                System.out.println(second);
                System.out.println("---");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
