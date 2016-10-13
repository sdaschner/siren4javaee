package com.sebastian_daschner.siren4javaee;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * Represents a Siren entity link.
 * This is created by the {@link com.sebastian_daschner.siren4javaee.EntityReader} or the {@link com.sebastian_daschner.siren4javaee.SirenClient}
 * functionality, respectively.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public class Link extends SirenObject {

    private final Set<String> rels = new HashSet<>();
    private URI href;
    private MediaType type;

    private Link() {
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

        final Link link = (Link) o;

        if (!rels.equals(link.rels)) return false;
        if (href != null ? !href.equals(link.href) : link.href != null) return false;
        return type != null ? type.equals(link.type) : link.type == null;
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
        return "Link{" +
                "rels=" + rels +
                ", href=" + href +
                ", type=" + type +
                ", classes=" + getClasses() +
                ", title=" + getTitle() +
                '}';
    }

    /**
     * Creates new {@link Link}s.
     * Only used by {@link EntityReader}.
     */
    static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder pattern to create new {@link Link}s.
     * Only used by {@link EntityReader}.
     */
    static class Builder extends SirenObject.Builder<Link, Builder> {

        private final Link link = new Link();

        @Override
        protected Link object() {
            return link;
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

        protected Link build() {
            return link;
        }

    }

}
