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

import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import static com.sebastian_daschner.siren4javaee.TestUtils.injectRuntimeDelegate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityBuilderTest {

    @Test
    public void testSimple() {
        final String actual = Siren.createEntityBuilder()
                .addClass("books")
                .addEntity(Siren.createEntityBuilder()
                        .addClass("book")
                        .addSubEntityRel("item")
                        .addProperty("name", "Java")
                        .addProperty("author", "Duke")
                        .addLink(URI.create("https://api.example.com/books/1"), "self"))
                .addLink(URI.create("https://api.example.com/books"), "self").build().toString();

        final String expected = "{\"class\":[\"books\"]," +
                "\"entities\":[" +
                "{" +
                "\"class\":[\"book\"]," +
                "\"rel\":[\"item\"]," +
                "\"properties\":{" +
                "\"name\":\"Java\"," +
                "\"author\":\"Duke\"}," +
                "\"links\":[{\"rel\":[\"self\"],\"href\":\"https://api.example.com/books/1\"}]" +
                "}]," +
                "\"links\":[{\"rel\":[\"self\"],\"href\":\"https://api.example.com/books\"}]}";
        assertThat(actual, is(expected));
    }

    @Test
    public void testProperties() {
        final String actual = Siren.createEntityBuilder()
                .addProperty("bigDecimal", BigDecimal.TEN)
                .addProperty("bigInteger", BigInteger.TEN)
                .addProperty("int", 5)
                .addProperty("long", 1_000_000_000L)
                .addProperty("boolean", true)
                .addProperty("double", 1.2d)
                .build().toString();

        final String expected = "{\"properties\":{" +
                "\"bigDecimal\":10," +
                "\"bigInteger\":10," +
                "\"int\":5," +
                "\"long\":1000000000," +
                "\"boolean\":true," +
                "\"double\":1.2}" +
                "}";
        assertThat(actual, is(expected));
    }

    @Test
    public void testComplex() {
        final String actual = Siren.createEntityBuilder()
                .addClass("books")
                .addEntity(Siren.createEntityBuilder()
                        .addClass("book")
                        .addSubEntityRel("item")
                        .addProperty("name", "Java")
                        .addProperty("author", "Duke")
                        .addLink(URI.create("https://api.example.com/books/1"), "self"))
                .addEntity(Siren.createEntityBuilder()
                        .addClass("book")
                        .addSubEntityRel("item")
                        .setTitle("Book")
                        .setSubEntityType("application/json")
                        .addProperty("name", "Hello")
                        .addProperty("author", "World")
                        .addLink(Siren.createLinkBuilder()
                                .addClass("link")
                                .addClass("class")
                                .addRel("self")
                                .addRel("foobar")
                                .setHref(URI.create("https://api.example.com/books/2"))
                                .setTitle("Link title")
                                .setType(MediaType.APPLICATION_JSON))
                        .addLink(Siren.createLinkBuilder()
                                .addClass("link")
                                .addRel("self")
                                .addRel("foobar")
                                .setHref(URI.create("https://api.example.com/books/2"))
                                .setTitle("Link title")
                                .setType(MediaType.APPLICATION_JSON).build())
                        .build())
                .addAction(Siren.createActionBuilder()
                        .setName("add-to-cart")
                        .setTitle("Add book to shopping cart")
                        .setMethod(HttpMethod.POST)
                        .setHref(URI.create("https://api.example.com/shopping_cart"))
                        .setType(MediaType.APPLICATION_JSON)
                        .addField("id", FieldType.TEXT)
                        .addField("quantity", FieldType.NUMBER))
                .addAction(Siren.createActionBuilder()
                        .addClass("action")
                        .addClass("class")
                        .setName("modify")
                        .setTitle("Modify book")
                        .setMethod(HttpMethod.PUT)
                        .setHref(URI.create("https://api.example.com/books/1"))
                        .setType(MediaType.APPLICATION_JSON)
                        .addField(Siren.createFieldBuilder()
                                .setName("name")
                                .setType(FieldType.COLOR)
                                .setValue("#ff0000")
                                .addClass("name-color")
                                .setTitle("Color"))
                        .addField(Siren.createFieldBuilder()
                                .addClass("foobar")
                                .addClass("test")
                                .setName("test")
                                .setRequired(true)
                                .setType(FieldType.DATETIME_LOCAL).build()))
                .addAction(Siren.createActionBuilder()
                        .addClass("action")
                        .setName("delete")
                        .setTitle("Delete book")
                        .setMethod(HttpMethod.DELETE)
                        .setHref(URI.create("https://api.example.com/books/1"))
                        .build())
                .addLink(URI.create("https://api.example.com/books"), "self").build().toString();

        // string have to match exactly -> JSON property order matters
        final String expected = "{" +
                "\"class\":[\"books\"]," +
                "\"entities\":[" +
                "{" +
                "\"class\":[\"book\"]," +
                "\"rel\":[\"item\"]," +
                "\"properties\":{\"name\":\"Java\"," +
                "\"author\":\"Duke\"}," +
                "\"links\":[{\"rel\":[\"self\"],\"href\":\"https://api.example.com/books/1\"}]}," +
                "{" +
                "\"class\":[\"book\"]," +
                "\"title\":\"Book\"," +
                "\"rel\":[\"item\"]," +
                "\"type\":\"application/json\"," +
                "\"properties\":{" +
                "\"name\":\"Hello\"," +
                "\"author\":\"World\"}," +
                "\"links\":[" +
                "{\"class\":[\"link\",\"class\"],\"title\":\"Link title\",\"rel\":[\"self\",\"foobar\"],\"href\":\"https://api.example.com/books/2\",\"type\":\"application/json\"}," +
                "{\"class\":[\"link\"],\"title\":\"Link title\",\"rel\":[\"self\",\"foobar\"],\"href\":\"https://api.example.com/books/2\",\"type\":\"application/json\"}" +
                "]" +
                "}]," +
                "\"links\":[{\"rel\":[\"self\"],\"href\":\"https://api.example.com/books\"}]," +
                "\"actions\":[" +
                "{" +
                "\"name\":\"add-to-cart\"," +
                "\"title\":\"Add book to shopping cart\"," +
                "\"method\":\"POST\"," +
                "\"href\":\"https://api.example.com/shopping_cart\"," +
                "\"type\":\"application/json\"," +
                "\"fields\":[" +
                "{\"name\":\"id\",\"type\":\"text\",\"required\":false}," +
                "{\"name\":\"quantity\",\"type\":\"number\",\"required\":false}]" +
                "},{" +
                "\"class\":[\"action\",\"class\"]," +
                "\"name\":\"modify\"," +
                "\"title\":\"Modify book\"," +
                "\"method\":\"PUT\"," +
                "\"href\":\"https://api.example.com/books/1\"," +
                "\"type\":\"application/json\"," +
                "\"fields\":[" +
                "{\"class\":[\"name-color\"],\"name\":\"name\",\"type\":\"color\",\"value\":\"#ff0000\",\"title\":\"Color\",\"required\":false}," +
                "{\"class\":[\"foobar\",\"test\"],\"name\":\"test\",\"type\":\"datetime-local\",\"required\":true}]" +
                "},{" +
                "\"class\":[\"action\"]," +
                "\"name\":\"delete\"," +
                "\"title\":\"Delete book\"," +
                "\"method\":\"DELETE\"," +
                "\"href\":\"https://api.example.com/books/1\"" +
                "}]" +
                "}";
        assertThat(actual, is(expected));
    }

    @Test
    public void testJAXRSTypes() {
        // MediaType#toString and Link.Builder call RuntimeDelegate
        final RuntimeDelegate delegateMock = mock(RuntimeDelegate.class);

        final RuntimeDelegate.HeaderDelegate<MediaType> headerDelegateMock = mock(RuntimeDelegate.HeaderDelegate.class);
        when(delegateMock.createHeaderDelegate(MediaType.class)).thenReturn(headerDelegateMock);
        final Link.Builder linkBuilderMock = mock(Link.Builder.class);
        when(delegateMock.createLinkBuilder()).thenReturn(linkBuilderMock);
        when(headerDelegateMock.toString(any())).thenReturn("application/json");
        when(linkBuilderMock.uri(anyString())).thenReturn(linkBuilderMock);
        when(linkBuilderMock.rel(anyString())).thenReturn(linkBuilderMock);
        when(linkBuilderMock.build()).thenReturn(new DummyLink("https://api.example.com/books/1", "self"),
                new DummyLink("https://api.example.com/books/123", "one", "two"));

        injectRuntimeDelegate(delegateMock);

        final String actual = Siren.createEntityBuilder()
                .setSubEntityType(MediaType.APPLICATION_JSON_TYPE)
                .setSubEntityHref(URI.create("https://api.example.com/books/1"))
                .addLink(Link.fromUri("https://api.example.com/books/1").rel("self").build())
                .addLink(Link.fromUri("https://api.example.com/books/123").rel("one two").build())
                .build().toString();
        final String expected = "{" +
                "\"type\":\"application/json\"," +
                "\"href\":\"https://api.example.com/books/1\"," +
                "\"links\":[{" +
                "\"rel\":[\"self\"]," +
                "\"href\":\"https://api.example.com/books/1\"" +
                "},{" +
                "\"rel\":[\"one\",\"two\"]," +
                "\"href\":\"https://api.example.com/books/123\"}]" +
                "}";

        assertThat(actual, is(expected));
    }

}