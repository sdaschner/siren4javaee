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

import javax.ws.rs.client.Client;

/**
 * Class to create Siren builders, clients and readers.
 * <p>
 * The builder construct JSONP {@link javax.json.JsonObject}s.
 * <b>Example:</b>
 * <pre>
 * Siren.createEntityBuilder()
 *     .addClass("book")
 *     .addProperty("isbn", "1-2345-3456")
 *     .addProperty("name", "Java")
 *     .addEntity(
 *         Siren.createEntityBuilder()
 *             .addProperty("test", 1)
 *             .addSubEntityRel("item")
 *             .addLink(URI.create(...), "other")
 *             .addAction(
 *                 Siren.createActionBuilder()...)
 *             .build())
 *     .addLink(uri, "self")
 *     .build();
 * </pre>
 * <p>
 * The client is used to retrieve entities and follow actions.
 * <b>Example:</b>
 * <pre>
 * SirenClient client = Siren.createClient(ClientBuilder.newClient());
 * Entity entity = client.retrieveEntity(uri);
 * JsonObject properties = ...
 * client.performAction(entity, "action-name", properties);
 * </pre>
 * <p>
 * The readers reads {@link javax.json.JsonObject}s into {@link Entity}s and is
 * usually just used indirectly in the {@link SirenClient}, but can also be used standalone.
 * <b>Example:</b>
 * <pre>
 * EntityReader reader = Siren.createEntityReader();
 * Entity entity = sirenReader.read(jsonObject);
 * </pre>
 *
 * @author Sebastian Daschner
 */
public final class Siren {

    private Siren() {
        throw new UnsupportedOperationException();
    }

    /**
     * Builder pattern factory method to create an empty {@link EntityBuilder}.
     * By calling {@link EntityBuilder#build()} the final JSONP object will be created.
     */
    public static EntityBuilder createEntityBuilder() {
        return new EntityBuilder();
    }

    /**
     * Builder pattern factory method to create an empty {@link LinkBuilder} which is used inside {@link EntityBuilder}s.
     * By calling {@link LinkBuilder#build()} the final JSONP object will be created.
     */
    public static LinkBuilder createLinkBuilder() {
        return new LinkBuilder();
    }

    /**
     * Builder pattern factory method to create an empty {@link ActionBuilder} which is used inside {@link EntityBuilder}s.
     * By calling {@link ActionBuilder#build()} the final JSONP object will be created.
     */
    public static ActionBuilder createActionBuilder() {
        return new ActionBuilder();
    }

    /**
     * Builder pattern factory method to create an empty {@link FieldBuilder} which is used inside {@link ActionBuilder}s.
     * By calling {@link FieldBuilder#build()} the final JSONP object will be created.
     */
    public static FieldBuilder createFieldBuilder() {
        return new FieldBuilder();
    }

    /**
     * Creates a new Siren client that uses the given {@code client} to access the locations.
     * Can retrieve {@link Entity}s and perform Siren actions.
     */
    public static SirenClient createClient(final Client client) {
        return new SirenClient(client);
    }

    /**
     * Creates a {@link EntityReader} that is used to read {@link javax.json.JsonObject}s into {@link Entity}s.
     * This class is usually used indirectly by using the {@link SirenClient}, but can also be used standalone.
     */
    public static EntityReader createEntityReader() {
        return new EntityReader();
    }

}
