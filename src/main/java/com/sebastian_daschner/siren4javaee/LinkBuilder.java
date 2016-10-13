/*
 * Copyright (C) 2016 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sebastian_daschner.siren4javaee;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * Builder pattern functionality to programmatically create link objects in Siren responses.
 * The {@link LinkBuilder} sub objects are created by calling {@link Siren#createLinkBuilder()},
 * modified by the available methods in this class and finally added to a Siren response entity by calling
 * {@link EntityBuilder#addLink(JsonObject)} or {@link EntityBuilder#addLink(LinkBuilder)}.
 *
 * @author Sebastian Daschner
 */
public class LinkBuilder {

    private JsonArrayBuilder relsBuilder;
    private JsonArrayBuilder classesBuilder;

    private String title;
    private URI href;
    private String type;

    LinkBuilder() {
        // prevent other instances than Siren factory methods
    }

    public LinkBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public LinkBuilder setHref(final URI href) {
        this.href = href;
        return this;
    }

    public LinkBuilder setType(final String type) {
        this.type = type;
        return this;
    }

    /**
     * <b>Note:</b> Works only in a JAX-RS implementation environment ({@link MediaType#toString()} calls the {@link javax.ws.rs.ext.RuntimeDelegate}).
     */
    public LinkBuilder setType(final MediaType type) {
        this.type = type.toString();
        return this;
    }

    public LinkBuilder addRel(final String rel) {
        if (relsBuilder == null)
            relsBuilder = Json.createArrayBuilder();
        relsBuilder.add(rel);
        return this;
    }

    public LinkBuilder addClass(final String entityClass) {
        if (classesBuilder == null)
            classesBuilder = Json.createArrayBuilder();
        classesBuilder.add(entityClass);
        return this;
    }

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (classesBuilder != null)
            objectBuilder.add("class", classesBuilder.build());
        if (title != null)
            objectBuilder.add("title", title);
        if (relsBuilder != null)
            objectBuilder.add("rel", relsBuilder.build());
        if (href != null)
            objectBuilder.add("href", href.toString());
        if (type != null)
            objectBuilder.add("type", type);

        return objectBuilder.build();
    }

}
