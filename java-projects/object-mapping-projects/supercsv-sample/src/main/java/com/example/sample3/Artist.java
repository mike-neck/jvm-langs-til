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

    @Cell(order = 1, header = "id", processor = Id.Processor.Factory.class)
    private final Id id;
    @Cell(order = 2, header = "アーティスト名", processor = Name.Processor.Factory.class)
    private final Name name;
    @Cell(order = 3, header = "活動国", processor = Country.CsvProcessor.Factory.class)
    private final Country country;
    @Cell(order = 4, header = "活動開始", processor = SinceYear.Processor.Factory.class)
    private final SinceYear since;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Cell(order = 5, header = "活動終了", processor = EndYear.Processor.Factory.class)
    private final Optional<EndYear> end;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Cell(order = 6, header = "レーベル", processor = Label.Processor.Factory.class)
    private final Optional<Label> label;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Cell(order = 7, header = "メンバー数", processor = Members.Processor.Factory.class)
    private final Optional<Members> members;
}
