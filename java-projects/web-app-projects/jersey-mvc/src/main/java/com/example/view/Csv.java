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

import com.example.processor.OptionalProcessor;
import com.example.processor.TypedCellProcessor;
import com.example.sample3.*;
import lombok.Getter;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
public class Csv<T> {

    private final Class<T> type;
    private final List<T> items;
    private final List<CellInfo> info;

    public Csv(Class<T> type, List<T> items) {
        this.type = type;
        this.items = items;
        this.info = collectInformation(type);
    }

    static <T> List<CellInfo> collectInformation(Class<T> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Cell.class))
                .map(Csv::filedToInfo)
                .sorted()
                .collect(toList());
    }

    static <T> CellInfo filedToInfo(Field field) {
        final Class<?> type = field.getType();
        final String name = field.getName();
        final Cell cell = field.getAnnotation(Cell.class);
        final Class<? extends CellProcessorFactory<?>> c = cell.processor();
        try {
            return Optional.ofNullable(c.newInstance())
                    .map(CellProcessorFactory::processor)
                    .map(p -> OptionalProcessor.create(isOptionalType(type), p))
                    .map(p -> new CellInfo(cell.order(), name, cell.header(), p))
                    .orElseThrow(() -> new InstantiationException("Cannot create CellProcessor instance."));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    static boolean isOptionalType(Class<?> type) {
        return Optional.class.equals(type);
    }

    public String[] getMapping() {
        return info.stream().map(CellInfo::getName).toArray(String[]::new);
    }

    public String[] getHeader() {
        return info.stream().map(CellInfo::getHeader).toArray(String[]::new);
    }

    public CellProcessor[] getProcessor() {
        return info.stream().map(CellInfo::getProcessor).toArray(CellProcessor[]::new);
    }
}
