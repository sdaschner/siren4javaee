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
 * Builder pattern functionality to programmatically create action objects in Siren responses.
 * The {@link ActionBuilder} sub objects are created by calling {@link Siren#createActionBuilder()},
 * modified by the available methods in this class and finally added to a Siren response entity by calling
 * {@link EntityBuilder#addAction(JsonObject)} or {@link EntityBuilder#addAction(ActionBuilder)}.
 *
 * @author Sebastian Daschner
 */
public class ActionBuilder {

    private JsonArrayBuilder fieldsBuilder;
    private JsonArrayBuilder classesBuilder;

    private String name;
    private String title;
    private String method;
    private URI href;
    private String type;

    ActionBuilder() {
        // prevent other instances than Siren factory methods
    }

    public ActionBuilder addClass(final String entityClass) {
        if (classesBuilder == null)
            classesBuilder = Json.createArrayBuilder();
        classesBuilder.add(entityClass);
        return this;
    }

    public ActionBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public ActionBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * <b>Note:</b> Constants can be accessed from {@link javax.ws.rs.HttpMethod}.
     */
    public ActionBuilder setMethod(final String method) {
        this.method = method;
        return this;
    }

    public ActionBuilder setHref(final URI href) {
        this.href = href;
        return this;
    }

    public ActionBuilder setType(final String type) {
        this.type = type;
        return this;
    }

    /**
     * <b>Note:</b> Works only in a JAX-RS implementation environment ({@link MediaType#toString()} calls the {@link javax.ws.rs.ext.RuntimeDelegate}).
     */
    public ActionBuilder setType(final MediaType type) {
        this.type = type.toString();
        return this;
    }

    /**
     * Shortcut to calling {@code actionBuilder.addField(Siren.createFieldBuilder().setName(name).setType(fieldType))}.
     */
    public ActionBuilder addField(final String name, final FieldType type) {
        addField(Siren.createFieldBuilder().setName(name).setType(type));
        return this;
    }

    public ActionBuilder addField(final FieldBuilder builder) {
        if (fieldsBuilder == null)
            fieldsBuilder = Json.createArrayBuilder();
        fieldsBuilder.add(builder.build());
        return this;
    }

    public ActionBuilder addField(final JsonObject field) {
        if (fieldsBuilder == null)
            fieldsBuilder = Json.createArrayBuilder();
        fieldsBuilder.add(field);
        return this;
    }

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (classesBuilder != null)
            objectBuilder.add("class", classesBuilder.build());

        if (name != null)
            objectBuilder.add("name", name);
        if (title != null)
            objectBuilder.add("title", title);
        if (method != null)
            objectBuilder.add("method", method);
        if (href != null)
            objectBuilder.add("href", href.toString());
        if (type != null)
            objectBuilder.add("type", type);
        if (fieldsBuilder != null)
            objectBuilder.add("fields", fieldsBuilder.build());

        return objectBuilder.build();
    }

}
