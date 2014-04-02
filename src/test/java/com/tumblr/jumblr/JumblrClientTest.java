package com.tumblr.jumblr;

import com.tumblr.jumblr.request.RequestBuilder;
import com.tumblr.jumblr.responses.ResponseWrapper;
import com.tumblr.jumblr.types.QuotePost;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for JumblrClient
 * @author jc
 */
public class JumblrClientTest {

    JumblrClient client;
    RequestBuilder builder;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws IOException {
        builder = mock(RequestBuilder.class);
        client = new JumblrClient("ck", "cs", "@", "@");
        client.setRequestBuilder(builder);
        ResponseWrapper rw = new MockResponseWrapper();
        when(builder.get(anyString(), anyMap())).thenReturn(rw);
        when(builder.post(anyString(), anyMap())).thenReturn(rw);
        when(builder.postMultipart(anyString(), anyMap())).thenReturn(rw);
        when(builder.getRedirectUrl(anyString())).thenReturn("url");
    }

    /**
     * User methods
     */

    @Test
    public void user() {
        client.user();
        verify(builder).get("/user/info", null);
    }

    @Test
    public void userDashboard() {
        client.userDashboard();
        verify(builder).get("/user/dashboard", null);

        Map<String, Object> options = getRandomishOptions();
        client.userDashboard(options);
        verify(builder).get("/user/dashboard", options);
    }

    @Test
    public void userFollowing() {
        client.userFollowing();
        verify(builder).get("/user/following", null);

        Map<String, Object> options = getRandomishOptions();
        client.userFollowing(options);
        verify(builder).get("/user/following", options);
    }

    @Test
    public void userLikes() {
        client.userLikes();
        verify(builder).get("/user/likes", null);

        Map<String, Object> options = getRandomishOptions();
        client.userLikes(options);
        verify(builder).get("/user/likes", options);
    }

    @Test
    public void like() {
        Long id = 42L;
        String reblogKey = "hello";

        client.like(id, reblogKey);
        Map<String, String> options = new HashMap<String, String>();
        options.put("id", id.toString());
        options.put("reblog_key", reblogKey);
        verify(builder).post("/user/like", options);
    }

    @Test
    public void unlike() {
        Long id = 42L;
        String reblogKey = "hello";

        client.unlike(id, reblogKey);
        Map<String, String> options = new HashMap<String, String>();
        options.put("id", id.toString());
        options.put("reblog_key", reblogKey);
        verify(builder).post("/user/unlike", options);
    }

    @Test
    public void follow() {
        client.follow("hey.com");
        Map<String, String> options = new HashMap<String, String>();
        options.put("url", "hey.com");
        verify(builder).post("/user/follow", options);
    }

    @Test
    public void unfollow() {
        client.unfollow("hey.com");
        Map<String, String> options = new HashMap<String, String>();
        options.put("url", "hey.com");
        verify(builder).post("/user/unfollow", options);
    }

    /**
     * Blog methods
     */

    @Test
    public void userAvatar() {
        client.blogAvatar("hey.com");
        verify(builder).getRedirectUrl("/blog/hey.com/avatar");

        client.blogAvatar("hey.com", 64);
        verify(builder).getRedirectUrl("/blog/hey.com/avatar/64");
    }

    @Test
    public void blogInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("api_key", "ck");

        client.blogInfo("blog_name");
        verify(builder).get("/blog/blog_name.tumblr.com/info", map);

        client.blogInfo("blog_name.com");
        verify(builder).get("/blog/blog_name.com/info", map);
    }

    @Test
    public void blogFollowers() {
        client.blogFollowers("blog_name");
        verify(builder).get("/blog/blog_name.tumblr.com/followers", null);

        Map<String, Object> options = getRandomishOptions();
        client.blogFollowers("blog_name", options);
        verify(builder).get("/blog/blog_name.tumblr.com/followers", options);
    }

    @Test
    public void blogLikes() {
        client.blogLikes("hey.com");
        verify(builder).get("/blog/hey.com/likes", getApiKeyOptions());

        Map<String, Object> options = getRandomishOptions();
        client.blogLikes("hey.com", options);
        options.putAll(getApiKeyOptions());
        verify(builder).get("/blog/hey.com/likes", options);
    }

    @Test
    public void blogPosts() {
        client.blogPosts("hey.com");
        verify(builder).get("/blog/hey.com/posts", getApiKeyOptions());

        Map<String, Object> options = getRandomishOptions();
        client.blogPosts("hey.com", options);
        options.putAll(getApiKeyOptions());
        verify(builder).get("/blog/hey.com/posts", options);

        options = getRandomishOptions();
        options.put("type", "audio");
        client.blogPosts("hey.com", options);
        options.remove("type"); // should not be there on the request
        options.putAll(getApiKeyOptions());
        verify(builder).get("/blog/hey.com/posts/audio", options);
    }

    @Test
    public void blogPost() {
        Long id = 24L;
        client.blogPost("hey.com", id);
        Map<String, Object> options = getApiKeyOptions();
        options.put("id", id.toString());
        verify(builder).get("/blog/hey.com/posts", options);
    }

    @Test
    public void blogQueuedPosts() {
        client.blogQueuedPosts("hey.com");
        verify(builder).get("/blog/hey.com/posts/queue", null);

        Map<String, Object> options = getRandomishOptions();
        client.blogQueuedPosts("hey.com", options);
        verify(builder).get("/blog/hey.com/posts/queue", options);
    }

    @Test
    public void blogDraftPosts() {
        client.blogDraftPosts("hey.com");
        verify(builder).get("/blog/hey.com/posts/draft", null);

        Map<String, Object> options = getRandomishOptions();
        client.blogDraftPosts("hey.com", options);
        verify(builder).get("/blog/hey.com/posts/draft", options);
    }

    @Test
    public void blogSubmissions() {
        client.blogSubmissions("hey.com");
        verify(builder).get("/blog/hey.com/posts/submission", null);

        Map<String, Object> options = getRandomishOptions();
        client.blogSubmissions("hey.com", options);
        verify(builder).get("/blog/hey.com/posts/submission", options);
    }

    /**
     * Post methods
     */

    @Test
    public void postDelete() {
        client.postDelete("hey.com", 42L);
        Map<String, String> options = new HashMap<String, String>();
        options.put("id", "42");
        verify(builder).post("/blog/hey.com/post/delete", options);
    }

    @Test
    public void postReblog() {
        client.postReblog("hey.com", 42L, "key");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("id", "42");
        options.put("reblog_key", "key");
        verify(builder).post("/blog/hey.com/post/reblog", options);

        options = getRandomishOptions();
        client.postReblog("hey.com", 42L, "key", options);
        options.put("id", "42");
        options.put("reblog_key", "key");
        verify(builder).post("/blog/hey.com/post/reblog", options);
    }

    @Test
    public void postEdit() throws IOException {
        Map<String, Object> options = getRandomishOptions();
        client.postEdit("hey.com", 42L, options);
        options.put("id", 42L);
        verify(builder).postMultipart("/blog/hey.com/post/edit", options);
    }

    @Test
    public void postCreate() throws IOException {
        Map<String, Object> options = getRandomishOptions();
        client.postCreate("hey.com", options);
        verify(builder).postMultipart("/blog/hey.com/post", options);
    }

    /**
     * Tagged methods
     */

    @Test
    public void tagged() {
        String tag = "coolio";

        client.tagged(tag);
        Map<String, Object> map = getApiKeyOptions();
        map.put("tag", tag);
        verify(builder).get("/tagged", map);

        Map<String, Object> options = getRandomishOptions();
        client.tagged(tag, options);
        options.putAll(getApiKeyOptions());
        options.put("tag", tag);
        verify(builder).get("/tagged", options);
    }

    /**
     * Other methods
     */

    @Test
    public void newPost() throws IllegalAccessException, InstantiationException {
        QuotePost post = client.newPost("blog", QuotePost.class);
        assertEquals("blog", post.getBlogName());
        assertEquals(client, post.getClient());
    }

    @Test
    public void setToken() {
        client.setToken("t1", "t2");
        verify(builder).setToken("t1", "t2");
    }

    @Test
    public void xauth() {
        client.xauth("email", "pass");
        verify(builder).postXAuth("email", "pass");
    }

    /**
     * Helper methods
     */

    private Map<String, Object> getApiKeyOptions() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("api_key", "ck");
        return map;
    }

    private Map<String, Object> getRandomishOptions() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hello", "world");
        return map;
    }

}