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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.stream.Stream;

/**
 * Builder pattern functionality to programmatically create Siren response entities.
 * The {@link EntityBuilder} is be created by calling {@link Siren#createEntityBuilder()},
 * modified by the available methods in this class and finally built to a {@link JsonObject} by calling {@link #build}.
 *
 * @author Sebastian Daschner
 */
public class EntityBuilder {

    private JsonArrayBuilder classesBuilder;
    private JsonObjectBuilder propertiesBuilder;
    private JsonArrayBuilder linksBuilder;
    private JsonArrayBuilder relBuilder;
    private JsonArrayBuilder subEntitiesBuilder;
    private JsonArrayBuilder actionsBuilder;

    private String title;
    private String type;
    private URI href;

    EntityBuilder() {
        // prevent other instances than Siren factory methods
    }

    public EntityBuilder addClass(final String entityClass) {
        if (classesBuilder == null)
            classesBuilder = Json.createArrayBuilder();
        classesBuilder.add(entityClass);
        return this;
    }

    public EntityBuilder addProperty(final String name, BigDecimal value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final BigInteger value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final int value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final long value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final boolean value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final double value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addProperty(final String name, final String value) {
        if (propertiesBuilder == null)
            propertiesBuilder = Json.createObjectBuilder();
        propertiesBuilder.add(name, value);
        return this;
    }

    public EntityBuilder addEntity(final EntityBuilder subBuilder) {
        if (subEntitiesBuilder == null)
            subEntitiesBuilder = Json.createArrayBuilder();
        subEntitiesBuilder.add(subBuilder.build());
        return this;
    }

    public EntityBuilder addEntity(final JsonObject subEntity) {
        if (subEntitiesBuilder == null)
            subEntitiesBuilder = Json.createArrayBuilder();
        subEntitiesBuilder.add(subEntity);
        return this;
    }

    public EntityBuilder addLink(final LinkBuilder builder) {
        if (linksBuilder == null)
            linksBuilder = Json.createArrayBuilder();
        linksBuilder.add(builder.build());
        return this;
    }

    public EntityBuilder addLink(final JsonObject link) {
        if (linksBuilder == null)
            linksBuilder = Json.createArrayBuilder();
        linksBuilder.add(link);
        return this;
    }

    public EntityBuilder addLink(final URI uri, final String rel) {
        if (linksBuilder == null)
            linksBuilder = Json.createArrayBuilder();
        linksBuilder.add(Json.createObjectBuilder()
                .add("rel", Json.createArrayBuilder().add(rel))
                .add("href", uri.toString())
                .build());
        return this;
    }

    public EntityBuilder addLink(final URI uri, final String... rel) {
        if (linksBuilder == null)
            linksBuilder = Json.createArrayBuilder();
        linksBuilder.add(Json.createObjectBuilder()
                .add("rel", Stream.of(rel).collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add))
                .add("href", uri.toString())
                .build());
        return this;
    }

    public EntityBuilder addLink(final Link link) {
        if (linksBuilder == null)
            linksBuilder = Json.createArrayBuilder();
        linksBuilder.add(Json.createObjectBuilder()
                .add("rel", link.getRels().stream().collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add))
                .add("href", link.getUri().toString())
                .build());
        return this;
    }

    public EntityBuilder addAction(final ActionBuilder builder) {
        if (actionsBuilder == null)
            actionsBuilder = Json.createArrayBuilder();
        actionsBuilder.add(builder.build());
        return this;
    }

    public EntityBuilder addAction(final JsonObject action) {
        if (actionsBuilder == null)
            actionsBuilder = Json.createArrayBuilder();
        actionsBuilder.add(action);
        return this;
    }

    public EntityBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public EntityBuilder addSubEntityRel(final String rel) {
        if (relBuilder == null)
            relBuilder = Json.createArrayBuilder();
        relBuilder.add(rel);
        return this;
    }

    public EntityBuilder setSubEntityType(final String type) {
        this.type = type;
        return this;
    }

    public EntityBuilder setSubEntityType(final MediaType type) {
        this.type = type.toString();
        return this;
    }

    public EntityBuilder setSubEntityHref(final URI href) {
        this.href = href;
        return this;
    }

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (classesBuilder != null)
            objectBuilder.add("class", classesBuilder.build());

        if (title != null)
            objectBuilder.add("title", title);
        if (relBuilder != null)
            objectBuilder.add("rel", relBuilder.build());
        if (type != null)
            objectBuilder.add("type", type);
        if (href != null)
            objectBuilder.add("href", href.toString());

        if (propertiesBuilder != null)
            objectBuilder.add("properties", propertiesBuilder.build());
        if (subEntitiesBuilder != null)
            objectBuilder.add("entities", subEntitiesBuilder.build());
        if (linksBuilder != null)
            objectBuilder.add("links", linksBuilder.build());
        if (actionsBuilder != null)
            objectBuilder.add("actions", actionsBuilder.build());

        return objectBuilder.build();
    }

}
