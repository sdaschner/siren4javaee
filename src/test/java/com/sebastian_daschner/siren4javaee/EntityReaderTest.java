package com.sebastian_daschner.siren4javaee;

import org.junit.Test;

import javax.json.JsonObject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;
import java.net.URI;

import static com.sebastian_daschner.siren4javaee.TestUtils.injectRuntimeDelegate;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityReaderTest {

    @Test
    public void testSimple() {
        final URI bookUri = URI.create("https://api.example.com/books/1");
        final URI booksUri = URI.create("https://api.example.com/books");
        final JsonObject jsonObject = Siren.createEntityBuilder()
                .addClass("books")
                .addEntity(Siren.createEntityBuilder()
                        .addClass("book")
                        .addSubEntityRel("item")
                        .addProperty("name", "Java")
                        .addProperty("author", "Duke")
                        .addLink(bookUri, "self"))
                .addLink(booksUri, "self").build();

        // builder only used for test, not meant for production code
        final Entity expected = Entity.newBuilder()
                .addClass("books")
                .addEntity(SubEntity.newBuilder()
                        .addClass("book")
                        .addRel("item")
                        .addProperty("name", "Java")
                        .addProperty("author", "Duke")
                        .addLink(Link.newBuilder().addRel("self").setHref(bookUri).build()).build())
                .addLink(Link.newBuilder().addRel("self").setHref(booksUri).build())
                .build();

        final EntityReader entityReader = Siren.createEntityReader();
        final Entity actual = entityReader.read(jsonObject);

        assertThat(actual, is(expected));
    }

    @Test
    public void testComplex() {
        // MediaType#fromString calls RuntimeDelegate
        final RuntimeDelegate delegateMock = mock(RuntimeDelegate.class);

        final RuntimeDelegate.HeaderDelegate<MediaType> headerDelegateMock = mock(RuntimeDelegate.HeaderDelegate.class);
        when(delegateMock.createHeaderDelegate(MediaType.class)).thenReturn(headerDelegateMock);
        when(headerDelegateMock.fromString(any())).thenReturn(MediaType.APPLICATION_JSON_TYPE);

        injectRuntimeDelegate(delegateMock);

        final JsonObject jsonObject = Siren.createEntityBuilder()
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
                        .addLink(URI.create("https://api.example.com/books/2"), "self", "foobar")
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
                        .addClass("action")
                        .addClass("class")
                        .setName("add-to-cart")
                        .setTitle("Add book to shopping cart")
                        .setMethod(HttpMethod.POST)
                        .setHref(URI.create("https://api.example.com/shopping_cart"))
                        .setType(MediaType.APPLICATION_JSON)
                        .addField("id", FieldType.TEXT)
                        .addField("quantity", FieldType.NUMBER))
                .addAction(Siren.createActionBuilder()
                        .addClass("action")
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
                        .setName("delete")
                        .setTitle("Delete book")
                        .setMethod(HttpMethod.DELETE)
                        .setHref(URI.create("https://api.example.com/books/1"))
                        .build())
                .addLink(URI.create("https://api.example.com/books"), "self")
                .build();

        final Entity expected = Entity.newBuilder()
                .addClass("books")
                .addEntity(SubEntity.newBuilder()
                        .addClass("book")
                        .addRel("item")
                        .addProperty("name", "Java")
                        .addProperty("author", "Duke")
                        .addLink(Link.newBuilder().addRel("self").setHref(URI.create("https://api.example.com/books/1")).build())
                        .build())
                .addEntity(SubEntity.newBuilder()
                        .addClass("book")
                        .addRel("item")
                        .setTitle("Book")
                        .setType(MediaType.APPLICATION_JSON_TYPE)
                        .addProperty("name", "Hello")
                        .addProperty("author", "World")
                        .addLink(Link.newBuilder().addRel("self").addRel("foobar").setHref(URI.create("https://api.example.com/books/2")).build())
                        .addLink(Link.newBuilder()
                                .addClass("link")
                                .addClass("class")
                                .addRel("self")
                                .addRel("foobar")
                                .setHref(URI.create("https://api.example.com/books/2"))
                                .setTitle("Link title")
                                .setType(MediaType.APPLICATION_JSON_TYPE)
                                .build())
                        .addLink(Link.newBuilder()
                                .addClass("link")
                                .addRel("self")
                                .addRel("foobar")
                                .setHref(URI.create("https://api.example.com/books/2"))
                                .setTitle("Link title")
                                .setType(MediaType.APPLICATION_JSON_TYPE).build())
                        .build())
                .addAction(Action.newBuilder()
                        .addClass("action")
                        .addClass("class")
                        .setName("add-to-cart")
                        .setTitle("Add book to shopping cart")
                        .setMethod(HttpMethod.POST)
                        .setHref(URI.create("https://api.example.com/shopping_cart"))
                        .setType(MediaType.APPLICATION_JSON_TYPE)
                        .addField(Field.newBuilder().setName("id").setType(FieldType.TEXT).build())
                        .addField(Field.newBuilder().setName("quantity").setType(FieldType.NUMBER).build())
                        .build())
                .addAction(Action.newBuilder()
                        .addClass("action")
                        .setName("modify")
                        .setTitle("Modify book")
                        .setMethod(HttpMethod.PUT)
                        .setHref(URI.create("https://api.example.com/books/1"))
                        .setType(MediaType.APPLICATION_JSON_TYPE)
                        .addField(Field.newBuilder()
                                .setName("name")
                                .setType(FieldType.COLOR)
                                .setValue("#ff0000")
                                .addClass("name-color")
                                .setTitle("Color")
                                .build())
                        .addField(Field.newBuilder()
                                .addClass("foobar")
                                .addClass("test")
                                .setName("test")
                                .setType(FieldType.DATETIME_LOCAL)
                                .setRequired(true)
                                .build())
                        .build())
                .addAction(Action.newBuilder()
                        .setName("delete")
                        .setTitle("Delete book")
                        .setMethod(HttpMethod.DELETE)
                        .setHref(URI.create("https://api.example.com/books/1"))
                        .build())
                .addLink(Link.newBuilder().addRel("self").setHref(URI.create("https://api.example.com/books")).build())
                .build();

        final Entity actual = Siren.createEntityReader().read(jsonObject);

        assertThat(actual, is(expected));
    }

}
