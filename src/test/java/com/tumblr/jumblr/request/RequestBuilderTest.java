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
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

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
        when(r.getCode()).thenReturn(200);
        when(r.getBody()).thenReturn("");

        thrown.expect(JumblrException.class);
        ResponseWrapper got = rb.clear(r);
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

}
