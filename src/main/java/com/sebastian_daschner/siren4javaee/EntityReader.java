package com.sebastian_daschner.siren4javaee;

import javax.json.*;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Reads Siren entities from JSONP {@link JsonObject}s.
 *
 * @author Sebastian Daschner
 */
public class EntityReader {

    EntityReader() {
        // prevent other instances than Siren factory methods
    }

    /**
     * Reads the {@code object} into an {@link Entity}.
     *
     * @throws RuntimeException If required information is not contained in the JSON.
     */
    public Entity read(final JsonObject object) {
        final Entity.Builder builder = Entity.newBuilder().setTitle(object.getString("title", null));

        readEntityStructures(object, builder);

        return builder.build();
    }

    private void readEntityStructures(final JsonObject object, final Entity.Builder builder) {
        readClasses(object.getJsonArray("class"), builder);
        readProperties(object.getJsonObject("properties"), builder);
        readSubEntities(object.getJsonArray("entities"), builder);
        readLinks(object.getJsonArray("links"), builder);
        readActions(object.getJsonArray("actions"), builder);
    }

    private void readClasses(final JsonArray jsonClasses, final SirenObject.Builder builder) {
        if (jsonClasses == null)
            return;

        jsonClasses.getValuesAs(JsonString.class).stream().map(JsonString::getString).forEach(builder::addClass);
    }

    private void readProperties(final JsonObject jsonProperties, final Entity.Builder builder) {
        if (jsonProperties == null)
            return;

        jsonProperties.forEach((k, v) -> builder.addProperty(k, getValue(k, jsonProperties)));
    }

    private Serializable getValue(final String key, final JsonObject jsonObject) {
        final JsonValue value = jsonObject.get(key);
        switch (value.getValueType()) {
            case STRING:
                return jsonObject.getString(key);
            case NUMBER:
                final JsonNumber number = jsonObject.getJsonNumber(key);
                if (number.isIntegral())
                    return number.longValue();
                return number.doubleValue();
            case TRUE:
                return true;
            case FALSE:
                return false;
        }
        return null;
    }

    private void readSubEntities(final JsonArray jsonEntities, final Entity.Builder builder) {
        if (jsonEntities == null)
            return;

        jsonEntities.getValuesAs(JsonObject.class).stream().map(object -> {

            final SubEntity.Builder subBuilder = SubEntity.newBuilder()
                    .setTitle(object.getString("title", null))
                    .setHref(getHref(object));

            if (object.containsKey("type"))
                subBuilder.setType(MediaType.valueOf(object.getString("type")));

            if (object.containsKey("rel"))
                object.getJsonArray("rel").getValuesAs(JsonString.class).stream().map(JsonString::getString).forEach(subBuilder::addRel);

            readEntityStructures(object, subBuilder);

            return subBuilder.build();
        }).forEach(builder::addEntity);
    }

    private void readLinks(final JsonArray jsonLinks, final Entity.Builder builder) {
        if (jsonLinks == null)
            return;

        jsonLinks.getValuesAs(JsonObject.class).stream().map(l -> {
            final JsonArray rels = l.getJsonArray("rel");
            if (rels == null || rels.isEmpty())
                throw new RuntimeException("At least one rels must be set for link " + l);

            final URI href = getHref(l);
            Objects.requireNonNull(href, () -> "href must be set for link " + l);
            final String title = l.getString("title", null);

            final Link.Builder linkBuilder = Link.newBuilder().setHref(href).setTitle(title);

            rels.getValuesAs(JsonString.class).stream().map(JsonString::getString).forEach(linkBuilder::addRel);
            readClasses(l.getJsonArray("class"), linkBuilder);
            if (l.containsKey("type"))
                linkBuilder.setType(MediaType.valueOf(l.getString("type")));

            return linkBuilder.build();
        }).forEach(builder::addLink);
    }

    private void readActions(final JsonArray jsonActions, final Entity.Builder builder) {
        if (jsonActions == null)
            return;

        jsonActions.getValuesAs(JsonObject.class).stream().map(a -> {
            final String name = a.getString("name", null);
            final String method = a.getString("method", null);
            final String title = a.getString("title", null);
            final URI href = getHref(a);
            Objects.requireNonNull(name, () -> "name must be set for action " + a);
            Objects.requireNonNull(href, () -> "href must be set for action " + a);

            final Action.Builder actionBuilder = Action.newBuilder()
                    .setTitle(title)
                    .setName(name)
                    .setMethod(method == null ? HttpMethod.GET : method)
                    .setHref(href);

            if (a.containsKey("type"))
                actionBuilder.setType(MediaType.valueOf(a.getString("type")));

            readFields(a.getJsonArray("fields"), actionBuilder);
            readClasses(a.getJsonArray("class"), actionBuilder);

            return actionBuilder.build();
        }).forEach(builder::addAction);
    }

    private URI getHref(final JsonObject object) {
        return object.containsKey("href") ? URI.create(object.getString("href", null)) : null;
    }

    private void readFields(final JsonArray jsonFields, final Action.Builder builder) {
        if (jsonFields == null)
            return;

        jsonFields.getValuesAs(JsonObject.class).stream().map(f -> {
            final String fieldName = f.getString("name", null);
            Objects.requireNonNull(fieldName, () -> "name must be set for field " + f);

            final FieldType type = FieldType.fromString(f.getString("type", null));

            final Field.Builder fieldBuilder = Field.newBuilder()
                    .setName(fieldName)
                    .setType(type == null ? FieldType.TEXT : type)
                    .setValue(f.getString("value", null))
                    .setTitle(f.getString("title", null))
                    .setRequired(f.getBoolean("required", false));

            readClasses(f.getJsonArray("class"), fieldBuilder);

            return fieldBuilder.build();
        }).forEach(builder::addField);
    }

}
