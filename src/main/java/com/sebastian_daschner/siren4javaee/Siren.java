package com.sebastian_daschner.siren4javaee;

/**
 * Class to create builders for Siren entity representations which build JSONP objects.
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
     * Builder pattern factory method to create an empty {@link ActionBuilder} which is used inside {@link EntityBuilder}s.
     * By calling {@link ActionBuilder#build()} the final JSONP object will be created.
     */
    public static ActionBuilder createActionBuilder() {
        return new ActionBuilder();
    }

}
