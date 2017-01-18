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
package com.example.controller;

import com.example.model.IndexModel;
import com.example.model.ServiceUser;
import com.example.qualifier.DateFormat;
import com.example.qualifier.TimeFormat;
import com.example.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/")
public class IndexController {

    private final DateTimeFormatter dateTimeFormat;
    private final DateTimeFormatter timeFormat;

    @Autowired
    public IndexController(
            @DateFormat DateTimeFormatter dateTimeFormat
            , @TimeFormat DateTimeFormatter timeFormat
    ) {
        this.dateTimeFormat = dateTimeFormat;
        this.timeFormat = timeFormat;
    }

    @RequestMapping(method = { RequestMethod.GET })
    ModelAndView index(@AuthenticationPrincipal(expression = "serviceUser") ServiceUser user) {
        final LocalDateTime now = LocalDateTime.now(user.getZoneId());
        final IndexModel model = new IndexModel(user.getUsername(), dateTimeFormat.format(now), timeFormat.format(now));
        return new ModelAndView(Views.INDEX.name(), "indexModel", model);
    }
}
