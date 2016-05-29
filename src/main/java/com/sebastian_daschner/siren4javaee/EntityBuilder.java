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

    private JsonArrayBuilder classBuilder;
    private JsonObjectBuilder propertiesBuilder;
    private JsonArrayBuilder linksBuilder;
    private JsonArrayBuilder relBuilder;
    private JsonArrayBuilder subEntitiesBuilder;
    private JsonArrayBuilder actionsBuilder;

    private String title;
    private String type;

    EntityBuilder() {
        // prevent other instances than Siren factory methods
    }

    public EntityBuilder addClass(final String entityClass) {
        if (classBuilder == null)
            classBuilder = Json.createArrayBuilder();
        classBuilder.add(entityClass);
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
                .add("rel", Json.createArrayBuilder().add(link.getRel()))
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

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (classBuilder != null)
            objectBuilder.add("class", classBuilder.build());

        if (title != null)
            objectBuilder.add("title", title);
        if (relBuilder != null)
            objectBuilder.add("rel", relBuilder.build());
        if (type != null)
            objectBuilder.add("type", type);

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
