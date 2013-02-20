package com.tumblr.jumblr.exceptions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.scribe.model.Response;

/**
 * This exception class is for any connection issue, it attempts to pull
 * a message out of the JSON response is possible
 * @author jc
 */
public class JumblrException extends RuntimeException {

    private final int responseCode;
    private String message;

    /**
     * Instantiate a new JumblrException given a bad response to wrap
     * @param response the response to wrap
     */
    public JumblrException(Response response) {
        this.responseCode = response.getCode();
        String body = response.getBody();

        JsonParser parser = new JsonParser();
        try {
            JsonObject object = parser.parse(body).getAsJsonObject();
            JsonObject meta = object.getAsJsonObject("meta");
            if (meta != null) {
                this.message = meta.getAsJsonPrimitive("msg").getAsString();
                return;
            }
            JsonPrimitive error = object.getAsJsonPrimitive("error");
            if (error != null) {
                this.message = error.getAsString();
                return;
            }
            throw new JsonParseException("Appropriate error field not found");
        } catch (JsonParseException ex) {
            this.message = body;
        }
    }

    /**
     * Get the HTTP response code for this error
     * @return the response code
     */
    public int getResponseCode() {
        return this.responseCode;
    }

    /**
     * Get the message for this error
     * @return the message
     */
    @Override
    public String getMessage() {
        return this.message;
    }

}
