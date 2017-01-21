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
package com.example.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class OptionalProcessor<T> extends CellProcessorAdaptor {

    private final TypedCellProcessor<T> child;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Supplier<T>> opt;

    public OptionalProcessor(TypedCellProcessor<T> child) {
        this.child = child;
    }

    public OptionalProcessor(TypedCellProcessor<T> child, Supplier<T> defaultValue) {
        this.child = child;
        this.opt = Optional.of(Objects.requireNonNull(defaultValue));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Object value, CsvContext context) {
        validateInputNotNull(value, context);
        if (value instanceof String) {
            return Optional.ofNullable(child.convert((String) value, context));
        } else if (value instanceof Optional) {
            return ((Optional<Object>) value)
                    .filter(child::typeCheck)
                    .map(o -> ((T) o))
                    .map(Optional::of)
                    .orElse(opt.map(Supplier::get))
                    .map(t -> child.convert(t, context))
                    .orElse("");
        } else {
            throw new IllegalStateException("expected Optional or String, but value is " + value);
        }
    }
}
