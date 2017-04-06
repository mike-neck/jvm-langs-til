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

import com.example.view.IndexView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("index")
public class IndexController {

    private final ZoneId zoneId;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public IndexController(ZoneId zoneId, DateTimeFormatter dateTimeFormatter) {
        this.zoneId = zoneId;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    ModelAndView index(@SessionAttribute Optional<User> user) {
        final IndexView indexView = user.map(u -> new IndexView(true, u.getUsername(), LocalDate.now(zoneId).format(dateTimeFormatter)))
                .orElseGet(() -> new IndexView(LocalDate.now(zoneId).format(dateTimeFormatter)));
        return new ModelAndView("index", "view", indexView);
    }
}
