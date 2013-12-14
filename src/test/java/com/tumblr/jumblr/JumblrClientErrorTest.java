package com.tumblr.jumblr;

import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.request.RequestBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JumblrClientErrorTest {

    JumblrClient client;
    RequestBuilder builder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws IOException {
        builder = mock(RequestBuilder.class);
        client = new JumblrClient("ck", "cs", "@", "@");
        client.setRequestBuilder(builder);
        when(builder.get(anyString(), anyMap())).thenThrow(JumblrException.class);
        when(builder.post(anyString(), anyMap())).thenThrow(JumblrException.class);
        when(builder.postMultipart(anyString(), anyMap())).thenThrow(JumblrException.class);
        when(builder.getRedirectUrl(anyString())).thenThrow(JumblrException.class);
    }
    /**
     * User methods
     */

    @Test
    public void user() {
        thrown.expect(JumblrException.class);
        client.user();
    }

    @Test
    public void userDashboard() {
        thrown.expect(JumblrException.class);
        client.userDashboard();
    }

    @Test
    public void userFollowing() {
        thrown.expect(JumblrException.class);
        client.userFollowing();
    }

    @Test
    public void userLikes() {
        thrown.expect(JumblrException.class);
        client.userLikes();
    }

    @Test
    public void like() {
        Long id = 42L;
        String reblogKey = "hello";

        thrown.expect(JumblrException.class);
        client.like(id, reblogKey);
    }

    @Test
    public void unlike() {
        Long id = 42L;
        String reblogKey = "hello";

        thrown.expect(JumblrException.class);
        client.unlike(id, reblogKey);
    }

    @Test
    public void follow() {
        thrown.expect(JumblrException.class);
        client.follow("hey.com");
    }

    @Test
    public void unfollow() {
        thrown.expect(JumblrException.class);
        client.unfollow("hey.com");
    }

    /**
     * Blog methods
     */

    @Test
    public void userAvatar() {
        thrown.expect(JumblrException.class);
        client.blogAvatar("hey.com");
    }

    @Test
    public void blogInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("api_key", "ck");

        thrown.expect(JumblrException.class);
        client.blogInfo("blog_name");
    }

    @Test
    public void blogFollowers() {
        thrown.expect(JumblrException.class);
        client.blogFollowers("blog_name");
    }

    @Test
    public void blogLikes() {
        thrown.expect(JumblrException.class);
        client.blogLikes("hey.com");
    }

    @Test
    public void blogPosts() {
        thrown.expect(JumblrException.class);
        client.blogPosts("hey.com");
    }

    @Test
    public void blogPost() {
        Long id = 24L;
        thrown.expect(JumblrException.class);
        client.blogPost("hey.com", id);
    }

    @Test
    public void blogQueuedPosts() {
        thrown.expect(JumblrException.class);
        client.blogQueuedPosts("hey.com");
    }

    @Test
    public void blogDraftPosts() {
        thrown.expect(JumblrException.class);
        client.blogDraftPosts("hey.com");
    }

    @Test
    public void blogSubmissions() {
        thrown.expect(JumblrException.class);
        client.blogSubmissions("hey.com");
    }

    /**
     * Post methods
     */

    @Test
    public void postDelete() {
        thrown.expect(JumblrException.class);
        client.postDelete("hey.com", 42L);
    }

    @Test
    public void postReblog() {
        thrown.expect(JumblrException.class);
        client.postReblog("hey.com", 42L, "key");
    }

    /**
     * Tagged methods
     */

    @Test
    public void tagged() {
        String tag = "coolio";

        thrown.expect(JumblrException.class);
        client.tagged(tag);
    }
}
