package com.sebastian_daschner.siren4javaee;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * Represents a Siren sub-entity.
 * This is created by the {@link com.sebastian_daschner.siren4javaee.EntityReader} or the {@link com.sebastian_daschner.siren4javaee.SirenClient}
 * functionality, respectively.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public class SubEntity extends Entity {

    private final Set<String> rels = new HashSet<>();
    private URI href;
    private MediaType type;

    private SubEntity() {
    }

    public Set<String> getRels() {
        return unmodifiableSet(rels);
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

        final SubEntity subEntity = (SubEntity) o;

        if (!rels.equals(subEntity.rels)) return false;
        if (href != null ? !href.equals(subEntity.href) : subEntity.href != null) return false;
        return type != null ? type.equals(subEntity.type) : subEntity.type == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + rels.hashCode();
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubEntity{" +
                "rels=" + rels +
                ", href=" + href +
                ", type=" + type +
                ", entities=" + getEntities() +
                ", actions=" + getActions() +
                ", links=" + getLinks() +
                ", properties=" + getProperties() +
                ", classes=" + getClasses() +
                ", title=" + getTitle() +
                '}';
    }

    /**
     * Creates new {@link SubEntity}s.
     * Only used by {@link EntityReader}.
     */
    static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder pattern to create new {@link SubEntity}s.
     * Only used by {@link EntityReader}.
     */
    static class Builder extends Entity.Builder<Builder> {

        private final SubEntity entity = new SubEntity();

        @Override
        protected SubEntity object() {
            return entity;
        }

        Builder addRel(final String rel) {
            object().rels.add(rel);
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

        protected SubEntity build() {
            return entity;
        }

    }

}
