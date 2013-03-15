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
 * Tests for Text Post
 * @author jc
 */
public class TextPostTest extends TypeTest {

    private TextPost post;

    private String title = "hello";
    private String body = "body";

    @Before
    public void setup() {
        Map<String, String> flat = new HashMap<String, String>();
        flat.put("type", "text");
        flat.put("title", title);
        flat.put("body", body);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (TextPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(title, post.getTitle());
        assertEquals(body, post.getBody());
    }

    @Test
    public void testWriters() {
        post.setTitle("test_title"); assertEquals("test_title", post.getTitle());
        post.setBody("test_body");   assertEquals("test_body", post.getBody());
    }

    @Test
    public void detail() {
        Map detail = post.detail();
        assertEquals(post.getTitle(), detail.get("title").toString());
        assertEquals(post.getBody(), detail.get("body").toString());
        assertEquals("text", detail.get("type"));
    }

}
