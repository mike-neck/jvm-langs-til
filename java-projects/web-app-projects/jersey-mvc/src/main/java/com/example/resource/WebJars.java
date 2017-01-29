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
package com.example.resource;

import com.example.Resource;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("webjars")
public class WebJars {

    private static final String WEBJARS = "META-INF/resources/webjars";

    @Path("filesaver/1.3.3")
    public FileSaver fileSaver() {
        return new FileSaver();
    }

    @Path("bootstrap.native/1.1.0")
    public BootstrapNative bootstrapNative() {
        return new BootstrapNative();
    }

    @Path("bootstrap-css-only/3.3.7")
    public BootstrapCssOnly bootstrapCssOnly() {
        return new BootstrapCssOnly();
    }

    @Path("whatwg-fetch/2.0.1")
    public WhatWgFetch whatWgFetch() {
        return new WhatWgFetch();
    }

    private interface Contents {
        String path();

        default String path(String file) {
            return WEBJARS + "/" + path() + "/" + file;
        }

        default Response utf8(String name) {
            return Response
                    .ok(file(path(name)))
                    .encoding("utf-8")
                    .build();
        }

        default Response binary(String name) {
            return Response
                    .ok(file(path(name)))
                    .build();
        }
    }

    public static class FileSaver implements Contents {

        @Override
        public String path() {
            return "filesaver/1.3.3";
        }

        @GET
        @Path("FileSaver.js")
        @Produces({"text/javascript"})
        public Response fileSaverJs() {
            return utf8("FileSaver.js");
        }

        @GET
        @Path("FileSaver.min.js")
        @Produces({"text/javascript"})
        public Response fileSaverMinJs() {
            return utf8("FileSaver.min.js");
        }
    }

    public static class BootstrapNative implements Contents {

        @Override
        public String path() {
            return "bootstrap.native/1.1.0/dist";
        }

        @GET
        @Path("dist/bootstrap-native.js")
        @Produces({"text/javascript"})
        public Response bootstrapNative() {
            return utf8("bootstrap-native.js");
        }

        @GET
        @Path("dist/bootstrap-native.min.js")
        @Produces({"text/javascript"})
        public Response bootstrapNativeMin() {
            return utf8("bootstrap-native.min.js");
        }
    }

    public static class BootstrapCssOnly implements Contents {

        @Override
        public String path() {
            return "bootstrap-css-only/3.3.7";
        }

        @GET
        @Path("css/bootstrap.css")
        @Produces({"text/css"})
        public Response bootstrapCss() {
            return utf8("css/bootstrap.css");
        }

        @GET
        @Path("css/bootstrap.min.css")
        @Produces({"text/css"})
        public Response bootstrapCssMin() {
            return utf8("css/bootstrap.min.css");
        }


        @GET
        @Path("css/bootstrap-theme.css")
        @Produces({"text/css"})
        public Response bootstrapThemeCss() {
            return utf8("css/bootstrap-theme.css");
        }

        @GET
        @Path("css/bootstrap-theme.min.css")
        @Produces({"text/css"})
        public Response bootstrapThemeCssMin() {
            return utf8("css/bootstrap-theme.min.css");
        }
    }

    public static class WhatWgFetch implements Contents {

        @Override
        public String path() {
            return "whatwg-fetch/2.0.1";
        }

        @GET
        @Path("fetch.js")
        @Produces({"text/javascript"})
        public Response fetchJs() {
            return utf8("fetch.js");
        }
    }

    @NotNull
    private static StreamingOutput file(String name) {
        return output -> Resource.copy(name, output);
    }
}
