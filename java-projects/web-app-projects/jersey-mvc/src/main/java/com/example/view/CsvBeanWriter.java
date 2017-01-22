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

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static com.example.Functions.consumer;

public class CsvBeanWriter implements Closeable {

    private final CsvDozerBeanWriter writer;

    public CsvBeanWriter(
            OutputStream out
            , String charset
    ) throws UnsupportedEncodingException {
        this(new OutputStreamWriter(out, charset)
                , CsvPreference.STANDARD_PREFERENCE);
    }

    public CsvBeanWriter(
            Writer writer
            , CsvPreference preference
    ) {
        this.writer = new CsvDozerBeanWriter(writer, preference);
    }

    public <T> void serializeCsv(Csv<T> csv) throws IOException {
        writer.configureBeanMapping(csv.getType(), csv.getMapping());
        writer.writeHeader(csv.getHeader());
        CellProcessor[] ps = csv.getProcessor();
        csv.getItems().forEach(consumer(c -> writer.write(c, ps)));
    }

    @Override
    public void close() throws IOException {
        writer.flush();
    }
}
