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

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class DummyLink extends Link {

    private final List<String> rels = new ArrayList<>();
    private final String uri;

    DummyLink(final String uri, final String... rels) {
        this.uri = uri;
        Stream.of(rels).forEach(this.rels::add);
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
        return rels.get(0);
    }

    @Override
    public List<String> getRels() {
        return rels;
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
