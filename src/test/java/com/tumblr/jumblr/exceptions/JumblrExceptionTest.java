package com.tumblr.jumblr.exceptions;

import com.tumblr.jumblr.types.JumblrError;
import org.junit.Test;
import org.scribe.model.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for JumblrException
 */
public class JumblrExceptionTest {

    @Test
    public void extractsMultiTypeErrors() {
        // given
        String content = "{\n" +
                "    \"meta\": {\n" +
                "        \"status\": 404,\n" +
                "        \"msg\": \"Not Found\"\n" +
                "    },\n" +
                "    \"response\": [],\n" +
                "    \"errors\": [\n" +
                "       \"test\"," +
                "        {\n" +
                "            \"title\": \"Not Found\",\n" +
                "            \"code\": 4012,\n" +
                "            \"detail\": \"This Tumblr is only viewable within the Tumblr dashboard\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Response response = mock(Response.class);
        when(response.getCode()).thenReturn(404);
        when(response.getBody()).thenReturn(content);

        // when
        JumblrException exception = new JumblrException(response);

        // then
        List<JumblrError> errors = exception.getErrors();
        assertNotNull(errors);
        assertEquals("Found two errors", errors.size(), 2);
        assertEquals("Found string error", "test", errors.get(0).getDetail());
        assertEquals("Found object error title", "Not Found", errors.get(1).getTitle());
        assertEquals("Found object error code", new Integer(4012), errors.get(1).getCode());
        assertEquals("Found object error detail", "This Tumblr is only viewable within the Tumblr dashboard", errors.get(1).getDetail());
    }

    @Test
    public void extractsErrorsWithEmptyArray() {
        // given
        String content = "{\n" +
                "    \"meta\": {\n" +
                "        \"status\": 404,\n" +
                "        \"msg\": \"Not Found\"\n" +
                "    },\n" +
                "    \"response\": [],\n" +
                "    \"errors\": []\n" +
                "}";

        Response response = mock(Response.class);
        when(response.getCode()).thenReturn(200);
        when(response.getBody()).thenReturn(content);

        // when
        JumblrException exception = new JumblrException(response);

        // then
        List<JumblrError> errors = exception.getErrors();
        assertNotNull(errors);
        assertEquals("Found no errors", errors.size(), 0);
    }

    @Test
    public void errorExtractionDoesNotFailWithNull() {
        // given
        String content = "{\n" +
                "    \"meta\": {\n" +
                "        \"status\": 404,\n" +
                "        \"msg\": \"Not Found\"\n" +
                "    },\n" +
                "    \"response\": []" +
                "}";

        Response response = mock(Response.class);
        when(response.getCode()).thenReturn(200);
        when(response.getBody()).thenReturn(content);

        // when
        JumblrException exception = new JumblrException(response);

        // then
        List<JumblrError> errors = exception.getErrors();
        assertNull(errors);
    }

}

