package com.tumblr.jumblr.types;

import com.google.gson.Gson;
import com.tumblr.jumblr.JumblrClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * User tests
 * @author jc
 */
public class UserTest extends TypeTest {

    JumblrClient client;
    User user;

    private String blogs = "[{ \"name\": \"name\" }]";
    private String name = "name";
    private Integer following = 12, likes = 12;
    private String default_post_format = "markdown"; // duh

    @Before
    public void setup() throws IOException {
        Map<String, Object> flat = new HashMap<String, Object>();
        flat.put("name", name);
        flat.put("following", following);
        flat.put("likes", likes);
        flat.put("blogs", blogs);
        flat.put("default_post_format", default_post_format);

        Gson gson = new Gson();
        user = gson.fromJson(flatSerialize(flat), User.class);
    }

    @Test
    public void testReaders() {
        assertEquals(default_post_format, user.getDefaultPostFormat());
        assertEquals(name, user.getName());
        assertEquals(likes, user.getLikeCount());
        assertEquals(following, user.getFollowingCount());

        Blog blog = user.getBlogs().get(0);
        assertEquals("name", blog.getName());
        assertEquals(client, blog.getClient());
    }

}
