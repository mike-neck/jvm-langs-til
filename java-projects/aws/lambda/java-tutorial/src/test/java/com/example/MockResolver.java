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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Parameter;

public class MockResolver implements ParameterResolver {

    @Override
    public boolean supports(ParameterContext pcx, ExtensionContext ecx) throws ParameterResolutionException {
        final Parameter parameter = pcx.getParameter();
        return parameter.isAnnotationPresent(Mock.class);
    }

    @Override
    public Object resolve(ParameterContext pcx, ExtensionContext ecx) throws ParameterResolutionException {
        final Parameter parameter = pcx.getParameter();
        final Class<?> type = parameter.getType();
        return Mockito.mock(type);
    }
}
