package com.sebastian_daschner.siren4javaee;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Client that reads Siren entities and performs actions.
 * The client is configured using a {@link Client}.
 *
 * @author Sebastian Daschner
 */
public class SirenClient {

    private final Client client;
    private final EntityReader entityReader;

    SirenClient(final Client client) {
        this.client = client;
        entityReader = Siren.createEntityReader();
    }

    /**
     * Gets a Siren entity following the {@code uri}.
     */
    public Entity retrieveEntity(final URI uri) {
        final JsonObject object = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get(JsonObject.class);
        return entityReader.read(object);
    }

    /**
     * Gets a Siren entity by following the link of the given {@code entity} that contains the given {@code rel}.
     * This is a shortcut for calling
     * <pre>
     *     URI uri = entity.getLink("rel");
     *     client.retrieveEntity(uri);
     * </pre>
     */
    public Entity followLink(final Entity entity, final String rel) {
        final URI uri = entity.getLink(rel);
        if (uri == null)
            throw new RuntimeException("Could not find link with rel " + rel + " in entity links");
        return retrieveEntity(uri);
    }

    /**
     * Performs the action contained in the {@code entity} with the given {@code action} name.
     * No properties are provided.
     *
     * @see #performAction(Entity, String, JsonObject)
     */
    public Response performAction(final Entity entity, final String action) {
        return performAction(entity, action, null);
    }

    /**
     * Performs the action contained in the {@code entity} with the given {@code action} name and field information contained in {@code properties}.
     */
    public Response performAction(final Entity entity, final String action, final JsonObject properties) {
        final Action entityAction = entity.getAction(action);
        if (entityAction == null)
            throw new RuntimeException("Could not find action with name " + action + " in entity");

        return performAction(entityAction, properties);
    }

    /**
     * Performs the given action.
     * No properties are provided.
     *
     * @see #performAction(Action, JsonObject)
     */
    public Response performAction(final Action action) {
        return performAction(action, null);
    }

    /**
     * Performs the given action with field information contained in {@code properties}.
     */
    public Response performAction(final Action action, final JsonObject properties) {
        final URI href = action.getHref();
        final String method = action.getMethod();
        final MediaType type = action.getType();

        if (type != null && !MediaType.APPLICATION_JSON_TYPE.isCompatible(type))
            throw new RuntimeException("Type " + type + " is not compatible with application/json");

        final Invocation.Builder invocation = client.target(href).request(MediaType.WILDCARD_TYPE);
        final javax.ws.rs.client.Entity<JsonObject> entity = action.getFields().isEmpty() ? null : buildJsonEntity(action, properties);

        final Response response = invoke(invocation, method, entity);

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
            throw new RuntimeException("Could not successfully perform action, HTTP status not successful, status: " + response.getStatus());

        return response;
    }

    private javax.ws.rs.client.Entity<JsonObject> buildJsonEntity(final Action action, final JsonObject properties) {
        final JsonObject entity = action.getFields().stream().collect(Json::createObjectBuilder, (b, f) -> {
            final String name = f.getName();
            addJsonValue(b, name, properties, f.isRequired());
        }, (c1, c2) -> c2.build().forEach(c1::add)).build();

        return javax.ws.rs.client.Entity.json(entity);
    }

    private void addJsonValue(final JsonObjectBuilder builder, final String name, final JsonObject properties, final boolean required) {
        final JsonValue value = properties == null ? null : properties.get(name);

        if (value == null) {
            if (required)
                throw new RuntimeException("Required field " + name + " not provided");
            return;
        }

        builder.add(name, value);
    }

    private Response invoke(final Invocation.Builder invocation, final String method, final javax.ws.rs.client.Entity<JsonObject> entity) {
        if (entity == null)
            return invocation.method(method);
        return invocation.method(method, entity);
    }

}
