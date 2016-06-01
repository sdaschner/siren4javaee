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

/**
 * Builder pattern functionality to programmatically create field objects in Siren response actions.
 * The {@link FieldBuilder} sub objects are created by calling {@link Siren#createFieldBuilder()},
 * modified by the available methods in this class and finally added to a Siren response entity by calling
 * {@link EntityBuilder#addAction(JsonObject)} or {@link EntityBuilder#addAction(ActionBuilder)}.
 *
 * @author Sebastian Daschner
 */
public class FieldBuilder {

    private JsonArrayBuilder classBuilder;
    private String name;
    private FieldType type;
    private String value;
    private String title;

    FieldBuilder() {
        // prevent other instances than Siren factory methods
    }

    public FieldBuilder addClass(final String fieldClass) {
        if (classBuilder == null)
            classBuilder = Json.createArrayBuilder();
        classBuilder.add(fieldClass);
        return this;
    }

    public FieldBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public FieldBuilder setType(final FieldType type) {
        this.type = type;
        return this;
    }

    public FieldBuilder setValue(final String value) {
        this.value = value;
        return this;
    }

    public FieldBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (classBuilder != null)
            objectBuilder.add("class", classBuilder.build());

        if (name != null)
            objectBuilder.add("name", name);
        if (type != null)
            objectBuilder.add("type", type.toString());
        if (value != null)
            objectBuilder.add("value", value);
        if (title != null)
            objectBuilder.add("title", title);

        return objectBuilder.build();
    }

}
