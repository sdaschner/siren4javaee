package com.sebastian_daschner.siren4javaee;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

/**
 * Represents a Siren entity.
 * This is created by the {@link com.sebastian_daschner.siren4javaee.EntityReader} or the {@link com.sebastian_daschner.siren4javaee.SirenClient}
 * functionality, respectively.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public class Entity extends SirenObject {

    private final List<SubEntity> entities = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();
    private final List<Link> links = new ArrayList<>();
    private final Map<String, Serializable> properties = new HashMap<>();

    protected Entity() {
    }

    public URI getLink(final String rel) {
        return getLinks().stream().filter(l -> l.getRels().contains(rel)).map(Link::getHref).findAny().orElse(null);
    }

    public Action getAction(final String name) {
        return getActions().stream().filter(a -> a.getName().equals(name)).findAny().orElse(null);
    }

    public List<SubEntity> getEntities() {
        return unmodifiableList(entities);
    }

    public List<Action> getActions() {
        return unmodifiableList(actions);
    }

    public List<Link> getLinks() {
        return unmodifiableList(links);
    }

    /**
     * Returns the properties of the entity. The values are either {@link String}s, {@link Boolean}s, {@link Long}s or {@link Double}s.
     */
    public Map<String, Serializable> getProperties() {
        return unmodifiableMap(properties);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final Entity entity = (Entity) o;

        if (!entities.equals(entity.entities)) return false;
        if (!actions.equals(entity.actions)) return false;
        if (!links.equals(entity.links)) return false;
        return properties.equals(entity.properties);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + entities.hashCode();
        result = 31 * result + actions.hashCode();
        result = 31 * result + links.hashCode();
        result = 31 * result + properties.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entities=" + entities +
                ", actions=" + actions +
                ", links=" + links +
                ", properties=" + properties +
                ", classes=" + getClasses() +
                ", title=" + getTitle() +
                '}';
    }

    /**
     * Creates new {@link Entity}s.
     * Only used by {@link EntityReader}.
     */
    static <T extends Builder<T>> Builder<T> newBuilder() {
        return new Builder<>();
    }

    /**
     * Builder pattern to create new {@link Entity}s.
     * Only used by {@link EntityReader}.
     */
    static class Builder<T extends Builder<T>> extends SirenObject.Builder<Entity, T> {

        private Entity entity;

        @Override
        protected Entity object() {
            // initialize lazily
            if (entity == null)
                entity = new Entity();
            return entity;
        }

        T addEntity(final SubEntity entity) {
            object().entities.add(entity);
            return (T) this;
        }

        T addAction(final Action action) {
            object().actions.add(action);
            return (T) this;
        }

        T addLink(final Link link) {
            object().links.add(link);
            return (T) this;
        }

        T addProperty(final String key, final Serializable value) {
            object().properties.put(key, value);
            return (T) this;
        }

        protected Entity build() {
            return entity;
        }

    }

}
