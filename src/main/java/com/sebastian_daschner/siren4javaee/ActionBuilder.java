package com.sebastian_daschner.siren4javaee;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * @author Sebastian Daschner
 */
public class ActionBuilder {

    private JsonArrayBuilder fieldsBuilder;

    private String name;
    private String title;
    private String method;
    private URI href;
    private String type;

    ActionBuilder() {
        // prevent other instances than Siren factory methods
    }

    public ActionBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ActionBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     * Constants can be accessed from {@link javax.ws.rs.HttpMethod}.
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

    // TODO add missing properties
    // TODO make Java type out of type
    public ActionBuilder addFields(final String name, final FieldType type) {
        if (fieldsBuilder == null)
            fieldsBuilder = Json.createArrayBuilder();
        fieldsBuilder.add(Json.createObjectBuilder().add("name", name).add("type", type.toString()));
        return this;
    }

    public JsonObject build() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

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
