package com.tumblr.jumblr.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tumblr.jumblr.responses.PostDeserializer;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for UnknownPost
 * @author jc
 */
public class UnknownTypePostTest extends TypeTest {

    private Post post;
    private Long id = 123L;

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "nonexistent");
        flat.put("id", id);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void isFound() {
        UnknownTypePost upost = (UnknownTypePost) post;
        assertEquals(upost.getId(), id);
    }

}
