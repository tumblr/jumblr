package com.tumblr.jumblr.request;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.responses.ResponseWrapper;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;

public class RequestBuilderTest {

    private RequestBuilder rb;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        rb = new RequestBuilder(null);
    }

    @Test
    public void testClearEmptyJson() {
        Response r = mock(Response.class);
        when(r.getBody()).thenReturn("");

        thrown.expect(JumblrException.class);
        ResponseWrapper got = rb.clear(r, 200);
    }

    @Test
    public void testGetParams() {
        JumblrClient client = new JumblrClient("abc", "def");
        client.getRequestBuilder().setHostname("test.com");
        Map<String, String> map = new HashMap<String, String>();
        map.put("limit", "1");
        OAuthRequest request = client.getRequestBuilder().constructGet("/path", map);

        assertEquals(request.getUrl(), "https://test.com/v2/path");
        assertEquals(request.getQueryStringParams().asFormUrlEncodedString(), "limit=1");
    }

    @Test
    public void testXauthForbidden() {
        Response r = mock(Response.class);
        when(r.getBody()).thenReturn("");

        thrown.expect(JumblrException.class);
        rb.clearXAuth(r, 403);
    }

    @Test
    public void testXauthSuccess() {
        Response r = mock(Response.class);
        when(r.getBody()).thenReturn("oauth_token=valueForToken&oauth_token_secret=valueForSecret");

        Token token = rb.clearXAuth(r, 200);
        assertEquals(token.getToken(), "valueForToken");
        assertEquals(token.getSecret(), "valueForSecret");
    }

    @Test
    public void testXauthSuccessWithExtra() {
        Response r = mock(Response.class);
        when(r.getBody()).thenReturn("oauth_token=valueForToken&oauth_token_secret=valueForSecret&other=paramisokay");

        Token token = rb.clearXAuth(r, 201);
        assertEquals(token.getToken(), "valueForToken");
        assertEquals(token.getSecret(), "valueForSecret");
    }

    @Test
    public void testXauthBadResponseGoodCode() {
        Response r = mock(Response.class);
        when(r.getBody()).thenReturn("");

        thrown.expect(JumblrException.class);
        rb.clearXAuth(r, 200);
    }

}
