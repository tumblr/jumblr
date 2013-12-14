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
    private Long noteCount = 321L;
    private Long rebloggedFromId = 123L;
    private String rebloggedFromName = "name";

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "text");
        flat.put("title", title);
        flat.put("body", body);
        flat.put("note_count", noteCount);
        flat.put("reblogged_from_id", rebloggedFromId);
        flat.put("reblogged_from_name", rebloggedFromName);
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
    public void testNoteCount() {
        assertEquals(noteCount, post.getNoteCount());
    }

    @Test
    public void testReblog() {
        assertEquals(rebloggedFromName, post.getRebloggedFromName());
        assertEquals(rebloggedFromId,   post.getRebloggedFromId());
    }

    @Test
    public void detail() {
        Map<String, Object> detail = post.detail();
        assertEquals(post.getTitle(), detail.get("title").toString());
        assertEquals(post.getBody(), detail.get("body").toString());
        assertEquals("text", detail.get("type"));
    }

}
