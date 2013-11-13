package com.tumblr.jumblr.request;

import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.responses.ResponseWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.scribe.model.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestBuilderTest {
    RequestBuilder rb;

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
}
