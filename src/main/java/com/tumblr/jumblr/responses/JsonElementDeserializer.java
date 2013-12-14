package com.tumblr.jumblr.responses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 *
 * @author jc
 */
public class JsonElementDeserializer implements JsonDeserializer<JsonElement> {

    @Override
    public JsonElement deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return je;
    }

}
