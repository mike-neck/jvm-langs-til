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
package com.example.story;

public enum Story {

    TEAM_ORGANIZATION_USER_CREATE_NEW_ACCOUNT(1, 1, "ユーザーは自身のアカウントを作成する"
            , "自身のemailアドレスを登録する"
            , "ユーザーはトークンをメールで受け取る"
    ),
    TEAM_ORGANIZATION_USER_MAIL_VERIFICATION(1, 2, "ユーザーは受け取ったメール記載の認証用URLからアカウントの登録を行う"
            , "名前、パスワードを登録する"
    ),
    TEAM_ORGANIZATION_USER_LOGIN(1, 3, "ユーザーはシステムにログインする"
            , "email、パスワードを入力する"
            , "存在しない組み合わせの場合はログインできない"
            , "存在する組み合わせの場合はログイン成功"
    ),
    TEAM_ORGANIZATION_CREATE_PAYMENT_METHOD(1, 4, "ユーザーは支払い方法を登録する"
            , "識別できる名前を登録する"
    ),
    TEAM_ORGANIZATION_TEAM_CREATION(1, 5, "ユーザーはチームを作成する"
            , "チーム名を登録する"
    ),
    TEAM_ORGANIZATION_INVITING_MEMBER(1, 6, "ユーザーは異なるユーザーにチーム権限とともに招待する"
            , "メンバーの権限、emailアドレスを登録する"
    ),
    TEAM_ORGANIZATION_MEMBER_REGISTRATION(1, 7, "招待されたユーザーは受け取ったメール記載の認証用URLにアクセスしてアカウントの登録を行う"
            , "名前、パスワードを登録する"
    );

    private final int topic;
    private final int theme;
    private final String title;
    private final String[] description;

    Story(int topic, int theme, String title, String... description) {
        this.topic = topic;
        this.theme = theme;
        this.title = title;
        this.description = description;
    }
}
