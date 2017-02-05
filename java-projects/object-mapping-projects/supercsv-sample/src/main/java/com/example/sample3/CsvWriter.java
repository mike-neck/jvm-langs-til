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
package com.example.sample3;

import com.example.processor.OptionalProcessor;
import com.example.processor.TypedCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.function.Functions.consumer;
import static java.util.stream.Collectors.toList;

public class CsvWriter<T> {

    private final Class<T> klass;
    private final List<CellInfo> cellInfoList;

    public CsvWriter(Class<T> klass) {
        this(klass, collectCellInfo(klass));
    }

    public CsvWriter(Class<T> klass, List<CellInfo> cellInfoList) {
        this.klass = klass;
        this.cellInfoList = cellInfoList;
    }

    static <T> List<CellInfo> collectCellInfo(Class<T> klass) {
        return Arrays.stream(klass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Cell.class))
                .map(CsvWriter::toInfo)
                .sorted()
                .collect(toList());
    }

    static CellInfo toInfo(Field field) {
        final String name = field.getName();
        final Class<?> type = field.getType();
        final Cell cell = field.getAnnotation(Cell.class);
        final Class<? extends CellProcessorFactory<?>> c = cell.processor();
        try {
            final CellProcessorFactory<?> f = c.newInstance();
            final TypedCellProcessor<?> processor = f.processor();
            final CellProcessor cp = OptionalProcessor.create(Optional.class.equals(type), processor);
            return new CellInfo(cell.order(), name, cell.header(), cp);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public ForDozerBeanWriter write(final List<T> beans) {
        return w -> {
            w.configureBeanMapping(klass, getMappers());
            w.writeHeader(getHeaders());
            final CellProcessor[] cp = getProcessors();
            beans.forEach(consumer(b -> w.write(b, cp)));
            w.flush();
        };
    }

    public String[] getHeaders() {
        return cellInfoList.stream()
                .map(CellInfo::getHeader)
                .toArray(String[]::new);
    }

    public String[] getMappers() {
        return cellInfoList.stream()
                .map(CellInfo::getName)
                .toArray(String[]::new);
    }

    public CellProcessor[] getProcessors() {
        return cellInfoList.stream()
                .map(CellInfo::getProcessor)
                .toArray(CellProcessor[]::new);
    }

    public interface ForDozerBeanWriter {
        void to(CsvDozerBeanWriter writer) throws IOException;
    }
}
