package com.tumblr.jumblr.responses;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tumblr.jumblr.types.UnknownTypePost;

/**
 * Posts come back to us as a collection, so this Deserializer is here
 * to make it so that the collection consists of the proper subclasses
 * of Post (ie: QuotePost, PhotoPost)
 * @author jc
 */
public class PostDeserializer implements JsonDeserializer<Object> {
	
	private Map<String, Class<?>> jsonTypeToPostType = new HashMap<String, Class<?>>();
	
    @Override
    public Object deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jobject = je.getAsJsonObject();
        String typeName = jobject.get("type").getAsString();
        
        Class<?> clazz = jsonTypeToPostType.get(typeName);
        if (clazz == null) {
        	clazz = getClassFor(typeName);
        	jsonTypeToPostType.put(typeName, clazz);
        }
        
        return jdc.deserialize(je, clazz);        
    }
    
    private Class<?> getClassFor(String typeName) {
        String className = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1) + "Post";
        try {
            return Class.forName("com.tumblr.jumblr.types." + className);
        } catch (ClassNotFoundException e) {
            System.out.println("deserialized post for unknown type: " + typeName);
            return UnknownTypePost.class;
        }
    }
}
