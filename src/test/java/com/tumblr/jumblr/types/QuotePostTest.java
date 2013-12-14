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
    private String author = "john";
    private String slug = "slug";
    private Long note_count = 123L;
    private String notes = "[{\"timestamp\":12345,\"blog_name\":\"bloggy\",\"blog_url\":\"bloggy_url\",\"type\":\"reblog_type\",\"post_id\":54321}]";

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "quote");
        flat.put("source", source);
        flat.put("text", text);
        flat.put("author", author);
        flat.put("slug", slug);
        flat.put("notes", notes);
        flat.put("note_count", 123);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (QuotePost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(source, post.getSource());
        assertEquals(text, post.getText());
        assertEquals(post.getAuthorId(), author);
        assertEquals(post.getSlug(), slug);
        assertEquals(post.getNoteCount(), note_count);
    }

    @Test
    public void testWriters() {
        post.setSource("test_source"); assertEquals("test_source", post.getSource());
        post.setText("test_text");   assertEquals("test_text", post.getText());
    }

    @Test
    public void testNotes() {
        Note note = post.getNotes().get(0);
        assertEquals(note.getTimestamp().toString(), "12345");
        assertEquals(note.getPostId().toString(), "54321");
        assertEquals(note.getBlogName(), "bloggy");
        assertEquals(note.getBlogUrl(), "bloggy_url");
        assertEquals(note.getType(), "reblog_type");
    }

    @Test
    public void detail() {
        Map<String, Object> detail = post.detail();
        assertEquals(post.getSource(), detail.get("source").toString());
        assertEquals(post.getText(), detail.get("quote").toString());
        assertEquals("quote", detail.get("type"));
    }

}
