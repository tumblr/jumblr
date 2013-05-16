package com.tumblr.jumblr.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tumblr.jumblr.responses.PostDeserializer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * VideoPost tests
 * @author jc
 */
public class VideoPostTest extends TypeTest {

    private String caption = "hello";
    private String videos = "[{\"width\":300,\"embed_code\":\"embed\"}]";

    private VideoPost post;

    @Before
    public void setup() {
        Map<String, String> flat = new HashMap<String, String>();
        flat.put("type", "video");
        flat.put("caption", caption);
        flat.put("player", videos);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (VideoPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(caption, post.getCaption());

        Video video = post.getVideos().get(0);
        assertEquals(new Integer(300), video.getWidth());
        assertEquals("embed", video.getEmbedCode());
    }

    @Test(expected=IllegalArgumentException.class)
    public void setDataWithEmbedCode() {
        post.setData(new File("some_path"));
        post.setEmbedCode("something");
    }

    @Test
    public void setDataWithoutEmbedCode() {
        File file = new File("some_path");
        post.setData(file);
        Map detail = post.detail();
        assertEquals(file, detail.get("data"));
        // clear
        post.setData(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void setEmbedCodeWithData() {
        post.setEmbedCode("something");
        post.setData(new File("some_path"));
    }

    @Test
    public void setEmbedCodeWithoutData() {
        String embedCode = "external";
        post.setEmbedCode(embedCode);
        Map detail = post.detail();
        assertEquals(embedCode, detail.get("embed"));
        // clear
        post.setEmbedCode(null);
    }

    @Test
    public void testOtherDetail() {
        post.setCaption("test_caption");

        Map detail = post.detail();
        assertEquals("test_caption", detail.get("caption"));
        assertEquals("video", detail.get("type"));
    }

}
