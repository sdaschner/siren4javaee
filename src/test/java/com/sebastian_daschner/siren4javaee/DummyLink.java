package com.sebastian_daschner.siren4javaee;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

class DummyLink extends Link {

    private final String uri;
    private final String rel;

    DummyLink(final String uri, final String rel) {
        this.uri = uri;
        this.rel = rel;
    }

    @Override
    public URI getUri() {
        return URI.create(uri);
    }

    @Override
    public UriBuilder getUriBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRel() {
        return rel;
    }

    @Override
    public List<String> getRels() {
        return singletonList(uri);
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getParams() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

}
