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
public class ChatPostTest extends TypeTest {

    private ChatPost post;

    private String title = "hello";
    private String body = "body";
    private String dialogue = "[{\"label\":\"label\",\"phrase\":\"phrase\",\"name\":\"name\"}]";

    @Before
    public void setup() {
        Map<String, String> flat = new HashMap<String, String>();
        flat.put("type", "chat");
        flat.put("title", title);
        flat.put("body", body);
        flat.put("dialogue", dialogue);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (ChatPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(title, post.getTitle());
        assertEquals(body, post.getBody());

        Dialogue d = post.getDialogue().get(0);
        assertEquals("name", d.getName());
        assertEquals("label", d.getLabel());
        assertEquals("phrase", d.getPhrase());
    }

    @Test
    public void testWriters() {
        post.setTitle("test_title"); assertEquals("test_title", post.getTitle());
        post.setBody("test_body");   assertEquals("test_body", post.getBody());
    }

    @Test
    public void detail() {
        Map<String, Object> detail = post.detail();
        assertEquals(post.getTitle(), detail.get("title").toString());
        assertEquals(post.getBody(), detail.get("conversation").toString());
        assertEquals("chat", detail.get("type"));
    }

}
