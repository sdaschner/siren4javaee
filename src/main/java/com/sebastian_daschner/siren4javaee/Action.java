package com.sebastian_daschner.siren4javaee;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Represents a Siren entity action.
 * This is created by the {@link com.sebastian_daschner.siren4javaee.EntityReader} or the {@link com.sebastian_daschner.siren4javaee.SirenClient}
 * functionality, respectively.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public class Action extends SirenObject {

    private final List<Field> fields = new ArrayList<>();
    private String name;
    private String method;
    private URI href;
    private MediaType type;

    private Action() {
    }

    public List<Field> getFields() {
        return unmodifiableList(fields);
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public URI getHref() {
        return href;
    }

    public MediaType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final Action action = (Action) o;

        if (!fields.equals(action.fields)) return false;
        if (name != null ? !name.equals(action.name) : action.name != null) return false;
        if (method != null ? !method.equals(action.method) : action.method != null) return false;
        if (href != null ? !href.equals(action.href) : action.href != null) return false;
        return type != null ? type.equals(action.type) : action.type == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + fields.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Action{" +
                "fields=" + fields +
                ", name='" + name + '\'' +
                ", method='" + method + '\'' +
                ", href=" + href +
                ", type=" + type +
                ", classes=" + getClasses() +
                ", title=" + getTitle() +
                '}';
    }

    /**
     * Creates new {@link Action}s.
     * Only used by {@link EntityReader}.
     */
    static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder pattern to create new {@link Action}s.
     * Only used by {@link EntityReader}.
     */
    static class Builder extends SirenObject.Builder<Action, Builder> {

        private final Action action = new Action();

        @Override
        protected Action object() {
            return action;
        }

        Builder addField(final Field field) {
            object().fields.add(field);
            return this;
        }

        Builder setName(final String name) {
            object().name = name;
            return this;
        }

        Builder setMethod(final String method) {
            object().method = method;
            return this;
        }

        Builder setHref(final URI href) {
            object().href = href;
            return this;
        }

        Builder setType(final MediaType type) {
            object().type = type;
            return this;
        }

        protected Action build() {
            return action;
        }

    }

}
