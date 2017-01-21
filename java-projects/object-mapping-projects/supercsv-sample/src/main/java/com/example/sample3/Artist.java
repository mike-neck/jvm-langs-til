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

import com.example.type.Country;
import com.example.type.EndYear;
import com.example.type.Id;
import com.example.type.Label;
import com.example.type.Members;
import com.example.type.Name;
import com.example.type.SinceYear;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class Artist {

    private final Id id;
    private final Name name;
    private final Country country;
    private final SinceYear since;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<EndYear> end;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Label> label;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Members> members;
}
