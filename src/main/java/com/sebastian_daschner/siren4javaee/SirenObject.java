package com.sebastian_daschner.siren4javaee;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * Base class for all Siren objects.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public abstract class SirenObject {

    private final Set<String> classes = new HashSet<>();
    private String title;

    protected SirenObject() {
    }

    public Set<String> getClasses() {
        return unmodifiableSet(classes);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SirenObject that = (SirenObject) o;

        if (!classes.equals(that.classes)) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = classes.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    /**
     * Builder pattern to create new {@link SirenObject}s.
     * Used by implementing classes.
     */
    static abstract class Builder<T extends SirenObject, V extends Builder<T, V>> {

        protected abstract T object();

        V addClass(final String entityClass) {
            final SirenObject object = object();
            object.classes.add(entityClass);
            return (V) this;
        }

        V setTitle(final String title) {
            final SirenObject object = object();
            object.title = title;
            return (V) this;
        }

        protected abstract T build();

    }

}
