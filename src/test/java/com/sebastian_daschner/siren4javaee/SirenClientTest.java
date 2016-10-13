package com.sebastian_daschner.siren4javaee;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.RuntimeDelegate;
import java.net.URI;

import static com.sebastian_daschner.siren4javaee.TestUtils.injectRuntimeDelegate;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SirenClientTest {

    private Client clientMock;
    private WebTarget targetMock;
    private Invocation.Builder invocationMock;
    private Response responseMock;
    private Entity expectedEntity;
    private JsonObject responseEntity;

    private SirenClient cut;
    private RuntimeDelegate.HeaderDelegate<MediaType> headerDelegateMock;

    @Test
    public void test() {
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);

        assertThat(entity, is(expectedEntity));

        cut.followLink(entity, "self");
        verify(invocationMock, times(2)).get(JsonObject.class);

        cut.performAction(entity, "delete");

        final JsonObject properties = Json.createObjectBuilder().add("hello", "world").add("test", "foobar").build();
        cut.performAction(entity, "delete", properties);

        cut.performAction(entity.getAction("delete"));
        verify(invocationMock, times(3)).method(HttpMethod.DELETE);

        cut.performAction(entity.getAction("modify"), properties);

        final ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        verify(clientMock, times(6)).target(uriCaptor.capture());

        assertThat(uriCaptor.getAllValues(), hasItems(
                uri,
                URI.create("https://api.example.com/books"),
                URI.create("https://api.example.com/books/2"),
                URI.create("https://api.example.com/books/2"),
                URI.create("https://api.example.com/books/1"),
                URI.create("https://api.example.com/books/1")));

        final ArgumentCaptor<String> methodCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<javax.ws.rs.client.Entity> entityCaptor = ArgumentCaptor.forClass(javax.ws.rs.client.Entity.class);
        verify(invocationMock).method(methodCaptor.capture(), entityCaptor.capture());

        assertThat(methodCaptor.getValue(), is(HttpMethod.PUT));
        final JsonObject actualProperties = (JsonObject) entityCaptor.getValue().getEntity();
        assertThat(actualProperties, is(Json.createObjectBuilder().add("test", "foobar").build()));
    }

    @Test
    public void testFieldNotProvided() {
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);
        try {
            cut.performAction(entity.getAction("modify"));
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Required field test not provided"));
            return;
        }
        fail("expected exception not occurred");
    }

    @Test
    public void testActionTypeNotJson() {
        when(headerDelegateMock.fromString(any())).thenReturn(MediaType.APPLICATION_XML_TYPE);
        when(headerDelegateMock.toString(any())).thenReturn(MediaType.APPLICATION_XML);
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);
        try {
            cut.performAction(entity.getAction("add-to-cart"));
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Type application/xml is not compatible with application/json"));
            return;
        }
        fail("expected exception not occurred");
    }

    @Test
    public void testActionNotSuccessful() {
        when(responseMock.getStatus()).thenReturn(400);
        when(responseMock.getStatusInfo()).thenReturn(Response.Status.BAD_REQUEST);
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);
        try {
            cut.performAction(entity.getAction("add-to-cart"));
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Could not successfully perform action, HTTP status not successful, status: 400"));
            return;
        }
        fail("expected exception not occurred");
    }

    @Test
    public void testLinkNotFound() {
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);
        try {
            cut.followLink(entity, "not-found");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Could not find link with rel not-found in entity links"));
            return;
        }
        fail("expected exception not occurred");
    }

    @Test
    public void testActionNotFound() {
        final URI uri = URI.create("http://example.com/resources/");

        cut = Siren.createClient(clientMock);
        Entity entity = cut.retrieveEntity(uri);
        try {
            cut.performAction(entity, "not-found");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Could not find action with name not-found in entity"));
            return;
        }
        fail("expected exception not occurred");
    }

    @Before
    public void setUp() {
        // MediaType#fromString calls RuntimeDelegate
        final RuntimeDelegate delegateMock = mock(RuntimeDelegate.class);

        headerDelegateMock = mock(RuntimeDelegate.HeaderDelegate.class);
        when(delegateMock.createHeaderDelegate(MediaType.class)).thenReturn(headerDelegateMock);
        when(headerDelegateMock.fromString(any())).thenReturn(MediaType.APPLICATION_JSON_TYPE);

        injectRuntimeDelegate(delegateMock);

        clientMock = mock(Client.class);
        targetMock = mock(WebTarget.class);
        invocationMock = mock(Invocation.Builder.class);
        responseMock = mock(Response.class);

        responseEntity = Siren.createEntityBuilder()
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
                        .setHref(URI.create("https://api.example.com/books/2"))
                        .build())
                .addLink(URI.create("https://api.example.com/books"), "self")
                .build();

        expectedEntity = Entity.newBuilder()
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
                                .setRequired(true)
                                .setType(FieldType.DATETIME_LOCAL)
                                .build())
                        .build())
                .addAction(Action.newBuilder()
                        .setName("delete")
                        .setTitle("Delete book")
                        .setMethod(HttpMethod.DELETE)
                        .setHref(URI.create("https://api.example.com/books/2"))
                        .build())
                .addLink(Link.newBuilder().addRel("self").setHref(URI.create("https://api.example.com/books")).build())
                .build();

        when(clientMock.target(any(URI.class))).thenReturn(targetMock);
        when(targetMock.request(any(MediaType.class))).thenReturn(invocationMock);

        when(invocationMock.get(JsonObject.class)).thenReturn(responseEntity);
        when(invocationMock.method(anyString())).thenReturn(responseMock);
        when(invocationMock.method(anyString(), any(javax.ws.rs.client.Entity.class))).thenReturn(responseMock);

        when(responseMock.getStatus()).thenReturn(200);
        when(responseMock.getStatusInfo()).thenReturn(Response.Status.OK);
    }

}
