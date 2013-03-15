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
 * AudioPost tests
 * @author jc
 */
public class AudioPostTest extends TypeTest {

    private String caption = "hello";
    private String trackName = "basketcase", albumName = "dookie";
    private String artistName = "green day", albumArtUrl = "http://google.com";
    private Integer plays = 24, year = 1997, trackNumber = 2;
    private String embedCode = "<code>is</code>";

    private AudioPost post;

    @Before
    public void setup() {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("type", "audio");
        flat.put("plays", plays);
        flat.put("caption", caption);
        flat.put("track_name", trackName);
        flat.put("album", albumName);
        flat.put("artist", artistName);
        flat.put("album_art", albumArtUrl);
        flat.put("year", year);
        flat.put("track_number", trackNumber);
        flat.put("player", embedCode);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (AudioPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void testReaders() {
        assertEquals(caption, post.getCaption());
        assertEquals(plays, post.getPlayCount());
        assertEquals(trackName, post.getTrackName());
        assertEquals(albumName, post.getAlbumName());
        assertEquals(artistName, post.getArtistName());
        assertEquals(albumArtUrl, post.getAlbumArtUrl());
        assertEquals(year, post.getYear());
        assertEquals(trackNumber, post.getTrackNumber());
        assertEquals(embedCode, post.getEmbedCode());
    }

    @Test(expected=IllegalArgumentException.class)
    public void setDataWithSource() {
        post.setData(new File("some_path"));
        post.setExternalUrl("something");
    }

    @Test
    public void setDataWithoutSource() {
        File file = new File("some_path");
        post.setData(file);
        Map<String, Object> detail = post.detail();
        assertEquals(file, detail.get("data"));
        // clear
        post.setData(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void setSourceWithData() {
        post.setExternalUrl("something");
        post.setData(new File("some_path"));
    }

    @Test
    public void setSourceWithoutData() {
        String externalUrl = "external";
        post.setExternalUrl(externalUrl);
        Map<String, Object> detail = post.detail();
        assertEquals(externalUrl, detail.get("external_url"));
        // clear
        post.setExternalUrl(null);
    }

    @Test
    public void testOtherDetail() {
        post.setCaption("test_caption");

        Map<String, Object> detail = post.detail();
        assertEquals("audio", detail.get("type"));
        assertEquals("test_caption", detail.get("caption"));
    }

}
