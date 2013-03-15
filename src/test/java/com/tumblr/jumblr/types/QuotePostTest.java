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
 * Tests for Quote Post
 * @author jc
 */
public class QuotePostTest extends TypeTest {

    private QuotePost post;

    private String source = "hello";
    private String text = "text";

    @Before
    public void setup() {
        Map<String, String> flat = new HashMap<String, String>();
        flat.put("type", "quote");
        flat.put("source", source);
        flat.put("text", text);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (QuotePost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(source, post.getSource());
        assertEquals(text, post.getText());
    }

    @Test
    public void testWriters() {
        post.setSource("test_source"); assertEquals("test_source", post.getSource());
        post.setText("test_text");   assertEquals("test_text", post.getText());
    }

    @Test
    public void detail() {
        Map<String, Object> detail = post.detail();
        assertEquals(post.getSource(), detail.get("source").toString());
        assertEquals(post.getText(), detail.get("quote").toString());
        assertEquals("quote", detail.get("type"));
    }

}
