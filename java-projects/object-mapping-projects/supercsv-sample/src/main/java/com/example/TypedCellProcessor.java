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
package com.example;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;

public abstract class TypedCellProcessor<T> extends CellProcessorAdaptor {

    private final Class<T> klass;

    public TypedCellProcessor(Class<T> klass) {
        super();
        this.klass = klass;
    }

    abstract protected String convert(T obj, CsvContext context);

    abstract protected T convert(String str, CsvContext context);

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Object value, CsvContext context) {
        validateInputNotNull(value, context);
        if (value instanceof String) {
            return next.execute(convert(((String) value), context), context);
        } else if (klass.isInstance(value)) {
            return next.execute(convert((T) value, context), context);
        } else {
            throw new IllegalStateException("expected String or " + klass.getSimpleName() + " but object is " + value);
        }
    }
}
