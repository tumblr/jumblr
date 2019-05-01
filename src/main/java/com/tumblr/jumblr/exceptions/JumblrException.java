package com.tumblr.jumblr.exceptions;

import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

import com.tumblr.jumblr.types.JumblrError;
import org.scribe.model.Response;


/**
 * This exception class is for any connection issue, it attempts to pull
 * a message out of the JSON response is possible
 * @author jc
 */
public class JumblrException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final int responseCode;
    private String message;
    private List<JumblrError> errors;

    /**
     * Instantiate a new JumblrException given a bad response to wrap
     * @param response the response to wrap
     */
    public JumblrException(Response response) {
        this.responseCode = response.getCode();
        String body = response.getBody();

        JsonParser parser = new JsonParser();
        try {
            final JsonElement element = parser.parse(body);
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                this.extractMessage(object);
                this.extractErrors(object);
            } else {
                this.message = body;
            }
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
     * @return the errors (or null if none)
     */
    public List<JumblrError> getErrors() {
        return this.errors;
    }

    /**
     * Pull the errors out of the response if present
     * @param object the parsed response object
     */
    private void extractErrors(JsonObject object) {
        JsonArray responseErrors;
        try {
            responseErrors = object.getAsJsonArray("errors");
        } catch (ClassCastException ex) {
            return; // errors is non-array
        }
        if (responseErrors == null) { return; }

        // Set the errors
        errors = new ArrayList<JumblrError>(responseErrors.size());
        for (int i = 0; i < responseErrors.size(); i++) {
            JsonElement errorElement = responseErrors.get(i);
            JumblrError error = new JumblrError();
            if (errorElement.isJsonObject()) {
                JsonObject errorJson = errorElement.getAsJsonObject();
                error.setTitle(errorJson.get("title").getAsString());
                error.setCode(errorJson.get("code").getAsInt());
                error.setDetail(errorJson.get("detail").getAsString());
            }
            else {
                error.setDetail(errorElement.getAsString());
            }
            errors.add(error);
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
        this.message = "Unknown JumblrError";
    }

}
