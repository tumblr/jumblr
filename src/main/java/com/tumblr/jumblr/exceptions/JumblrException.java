package com.tumblr.jumblr.exceptions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.scribe.model.Response;

public class JumblrException extends RuntimeException {
    
    private final int responseCode;
    private String message;
    
    public JumblrException(Response response) {
        this.responseCode = response.getCode();
        String body = response.getBody();
        
        String msg;
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = parser.parse(body).getAsJsonObject();
            JsonObject meta = object.getAsJsonObject("meta");
            this.message = meta.getAsJsonPrimitive("msg").getAsString();            
        } catch (JsonParseException ex) {
            this.message = body;
        }
    }
    
    public int getResponseCode() {
        return this.responseCode;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
}
