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
package com.example.view;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

@Provider
@Produces({"text/csv", MediaType.TEXT_PLAIN})
public class CsvWriter implements MessageBodyWriter<Csv<?>> {

    private static final Set<String> SUPPORTED_CHARSET = unmodifiableSet(
            new HashSet<>(
                    Arrays.asList(
                            "UTF-8"
                            , "Windows-31J"
                    )
            )
    );

    @Override
    public boolean isWriteable(Class<?> type, Type genericType
            , Annotation[] annotations, MediaType mediaType) {
        return Csv.class.equals(type);
    }

    @Override
    public long getSize(Csv<?> csv, Class<?> type, Type genericType
            , Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Csv<?> csv, Class<?> type, Type genericType
            , Annotation[] annotations, MediaType mediaType
            , MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        final Optional<String> chOpt = Optional.ofNullable(mediaType.getParameters().get("charset"));
        final String charset = chOpt.filter(SUPPORTED_CHARSET::contains)
                .orElse("UTF-8");
        try(final CsvBeanWriter writer = new CsvBeanWriter(entityStream, charset)) {
            writer.serializeCsv(csv);
        }
    }
}
