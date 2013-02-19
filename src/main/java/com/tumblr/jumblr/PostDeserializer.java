package com.tumblr.jumblr;

import com.google.gson.*;
import com.tumblr.jumblr.types.UnknownTypePost;
import java.lang.reflect.Type;

/**
 *
 * @author john
 */
public class PostDeserializer implements JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jobject = je.getAsJsonObject();
        String typeName = jobject.get("type").getAsString();
        String className = typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "Post";
        try {
            Class<?> clz = Class.forName("com.tumblr.jumblr.types." + className);
            return jdc.deserialize(je, clz);
        } catch (ClassNotFoundException e) {
            System.out.println("deserialized post for unknown type: " + typeName);
            return jdc.deserialize(je, UnknownTypePost.class);
        }
    }

}
