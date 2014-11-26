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
 * PostcardPost tests
 * @author jc
 */
public class PostcardPostTest extends TypeTest {

    private PostcardPost post;

    private String askingName = "john";
    private String askingUrl = "https://tumblr.com";
    private String body = "postcard body";

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "postcard");
        flat.put("asking_name", askingName);
        flat.put("asking_url", askingUrl);
        flat.put("body", body);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (PostcardPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(body, post.getBody());
        assertEquals(askingName, post.getAskingName());
        assertEquals(askingUrl, post.getAskingUrl());
    }

    @Test
    public void testWriters() {
        post.setBody("body"); assertEquals("body", post.getBody());
        post.setAskingUrl("url"); assertEquals("url", post.getAskingUrl());
        post.setAskingName("name"); assertEquals("name", post.getAskingName());
    }

    @Test
    public void detail() {
        Map<String, Object> detail = post.detail();
        assertEquals(post.getAskingUrl(), detail.get("asking_url").toString());
        assertEquals(post.getAskingName(), detail.get("asking_name").toString());
        assertEquals(post.getBody(), detail.get("body").toString());
        assertEquals("postcard", detail.get("type"));
    }

}
