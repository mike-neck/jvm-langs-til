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

import org.glassfish.jersey.internal.util.collection.Ref;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@javax.ws.rs.ext.Provider
public class ThymeleafTemplateProcessor implements TemplateProcessor<String> {

    public static final String PREFIX = "WEB-INF/templates";

    private final Configuration configuration;
    private final ServletContext servletContext;
    private final TemplateEngine engine;

    @Inject
    public ThymeleafTemplateProcessor(
            Configuration configuration
            , ServletContext servletContext
    )  {
        this.configuration = configuration;
        this.servletContext = servletContext;
        final ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix(PREFIX);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setSuffix(".html");
        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.addDialect(new Java8TimeDialect());
    }

    @Inject
    private Provider<Ref<HttpServletRequest>> request;

    @Inject
    private Provider<Ref<HttpServletResponse>> response;

    @Override
    public String resolve(String name, MediaType mediaType) {
        return name;
    }

    @Override
    public void writeTo(String templateReference
            , Viewable viewable
            , MediaType mediaType
            , MultivaluedMap<String, Object> httpHeaders
            , OutputStream out) throws IOException {
        final HttpServletRequest req = this.request.get().get();
        final WebContext context = new WebContext(req, response.get().get(), servletContext, req.getLocale());
        final Object model = viewable.getModel();
        if (model instanceof Map) {
            //noinspection unchecked
            context.setVariables(((Map<String, Object>) model));
        } else {
            final Map<String, Object> map = new HashMap<>();
            map.put("model", model);
            context.setVariables(map);
        }
        try (final Writer writer = new OutputStreamWriter(out)) {
            engine.process(viewable.getTemplateName(), context, writer);
        }
    }
}
