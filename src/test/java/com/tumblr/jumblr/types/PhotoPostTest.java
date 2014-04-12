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
 * PhotoPost tests
 * @author jc
 */
public class PhotoPostTest extends TypeTest {

    private String caption = "hello";
    private Integer width = 300, height = 500;

    private String photos = "[{\"caption\":\"caption1\",\"alt_sizes\":[{\"url\":\"url\",\"width\":400,\"height\":401}],\"original_size\":{\"width\":1,\"height\":2}}]";

    private PhotoPost post;

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "photo");
        flat.put("caption", caption);
        flat.put("width", width);
        flat.put("height", height);
        flat.put("photos", photos);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (PhotoPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(caption, post.getCaption());
        assertEquals(width, post.getWidth());
        assertEquals(height, post.getHeight());
        assertEquals(false, post.isPhotoset());

        Photo photo = post.getPhotos().get(0);
        assertEquals("caption1", photo.getCaption());

        PhotoSize size = photo.getSizes().get(0);
        assertEquals(400, size.getWidth());
        assertEquals(401, size.getHeight());
        assertEquals("url", size.getUrl());
    }

    @Test(expected=IllegalArgumentException.class)
    public void setDataWithSource() {
        post.setData(new File("some_path"));
        post.setSource("something");
    }

    @Test
    public void setDataWithoutSource() {
        File file = new File("some_path");
        post.setData(file);
        Map<String, Object> detail = post.detail();
        assertEquals(file, detail.get("data[0]"));
        // clear
        this.setup();
    }

    @Test(expected=IllegalArgumentException.class)
    public void setSourceWithData() {
        post.setSource("something");
        post.setData(new File("some_path"));
    }

    @Test
    public void setSourceWithoutData() {
        String embedCode = "external";
        post.setSource(embedCode);
        Map<String, Object> detail = post.detail();
        assertEquals(embedCode, detail.get("source"));
        // clear
        this.setup();
    }

    @Test
    public void testOtherDetail() {
        post.setCaption("test_caption");
        post.setLinkUrl("link url");

        Map<String, Object> detail = post.detail();
        assertEquals("test_caption", detail.get("caption"));
        assertEquals("link url", detail.get("link"));
        assertEquals("photo", detail.get("type"));
        assertEquals(1, post.getPhotos().get(0).getOriginalSize().getWidth());
        assertEquals(2, post.getPhotos().get(0).getOriginalSize().getHeight());
    }

}
