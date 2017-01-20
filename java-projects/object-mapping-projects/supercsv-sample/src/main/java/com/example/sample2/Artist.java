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
package com.example.sample2;

import com.example.type.Country;
import com.example.type.Country.CsvProcessor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Data
@RequiredArgsConstructor
public class Artist {

    private final int id;
    @org.jetbrains.annotations.NotNull
    private final String name;
    @org.jetbrains.annotations.NotNull
    private final Country country;
    @org.jetbrains.annotations.NotNull
    private final Integer since;
    @Nullable
    private final Integer end;
    @Nullable
    private final String label;
    @Nullable
    private final Integer members;

    public static final String[] MAPPER = {
            "id"
            , "name"
            , "country"
            , "since"
            , "end"
            , "label"
            , "members"
    };

    public static final String[] HEADER = {
            "id"
            , "アーティスト名"
            , "活動地"
            , "活動開始"
            , "活動終了"
            , "レーベル"
            , "メンバー"
    };

    public static final CellProcessor[] PROCESSORS = {
            new NotNull()
            , new NotNull()
            , new Country.CsvProcessor()
            , new NotNull()
            , new Optional()
            , new Optional()
            , new ConvertNullTo(0, new FmtNumber("0"))
    };
}
