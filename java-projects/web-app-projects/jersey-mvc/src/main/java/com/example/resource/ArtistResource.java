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
package com.example.resource;

import com.example.sample3.Artist;
import com.example.type.Country;
import com.example.view.Csv;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static com.example.sample3.Main.id;

@Path("artist")
public class ArtistResource {

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response artistsCsv() {
        return Response.ok(new Csv<>(Artist.class, artists()), "text/csv")
                .header("Content-Disposition", "attachment; filename = artist.csv")
                .build();
    }

    private static List<Artist> artists() {
        return Arrays.asList(
                id(1).name("Hallucinogen").country(Country.UK)
                        .since(1995).end(2005).emptyLabel().emptyMembers()
                , id(2).name("Infected Mushroom").country(Country.ISRAEL)
                        .since(1996).emptyEnd().label("BNE").members(2)
                , id(3).name("Juno Reactor").country(Country.ENGLAND)
                        .since(1993).emptyEnd().emptyLabel().emptyMembers()
        );
    }
}
