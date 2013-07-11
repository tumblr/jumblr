package com.tumblr.jumblr.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.scribe.model.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * This exception class is for any connection issue, it attempts to pull
 * a message out of the JSON response is possible
 * @author jc
 */
public class JumblrException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final int responseCode;
    private String message;
    private List<String> errors;

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
            this.extractMessage(object);
            this.extractErrors(object);
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

    /**
     * Get the errors returned from the API
     * @return List<String> errors (or null if none)
     */
    public List<String> getErrors() {
        return this.errors;
    }

    /**
     * Pull the errors out of the response if present
     * @param object the parsed response object
     */
    private void extractErrors(JsonObject object) {
        JsonObject response;
        try {
            response = object.getAsJsonObject("response");
        } catch (ClassCastException ex) {
            return; // response is non-object
        }
        if (response == null) { return; }

        JsonArray e = response.getAsJsonArray("errors");
        if (e == null) { return; }

        // Set the errors
        errors = new ArrayList<String>(e.size());
        for (int i = 0; i < e.size(); i++) {
            errors.add(e.get(i).getAsString());
        }
    }

    /**
     * Pull the message out of the response
     * @param object the parsed response object
     */
    private void extractMessage(JsonObject object) {
        // Prefer to pull the message out of meta
        JsonObject meta = object.getAsJsonObject("meta");
        if (meta != null) {
            JsonPrimitive msg = meta.getAsJsonPrimitive("msg");
            if (msg != null) {
                this.message = msg.getAsString();
                return;
            }
        }

        // Fall back on error
        JsonPrimitive error = object.getAsJsonPrimitive("error");
        if (error != null) {
            this.message = error.getAsString();
            return;
        }

        // Otherwise set a default
        this.message = "Unknown Error";
    }

}
