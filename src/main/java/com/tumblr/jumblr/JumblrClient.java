package com.tumblr.jumblr;

import com.tumblr.jumblr.request.RequestBuilder;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import org.scribe.model.Token;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the base JumblrClient that is used to make requests to the Tumblr
 * API.  All calls that can be made from other Resource(s) can be made from
 * here.
 * @author jc
 */
public class JumblrClient {

    private RequestBuilder requestBuilder;
    private String apiKey;

    public JumblrClient() {
        this.requestBuilder = new RequestBuilder(this);
    }

    /**
     * Instantiate a new Jumblr Client with no token
     * @param consumerKey The consumer key for the client
     * @param consumerSecret The consumer secret for the client
     */
    public JumblrClient(String consumerKey, String consumerSecret) {
        this();
        this.requestBuilder.setConsumer(consumerKey, consumerSecret);
        this.apiKey = consumerKey;
    }

    /**
     * Instantiate a new Jumblr Client
     * @param consumerKey The consumer key for the client
     * @param consumerSecret The consumer secret for the client
     * @param token The token for the client
     * @param tokenSecret The token secret for the client
     */
    public JumblrClient(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this(consumerKey, consumerSecret);
        this.setToken(token, tokenSecret);
    }

    /**
     * Set the token for this client
     * @param token The token for the client
     * @param tokenSecret The token secret for the client
     */
    public void setToken(String token, String tokenSecret) {
        this.requestBuilder.setToken(token, tokenSecret);
    }

    /**
     * Set the token for this client.
     * @param token The token for the client.
     */
    public void setToken(final Token token) {
        this.requestBuilder.setToken(token);
    }

    /**
     * Performs an XAuth authentication.
     * @param email the user's login email.
     * @param password the user's login password.
     */
    public void xauth(final String email, final String password) {
        setToken(this.requestBuilder.postXAuth(email, password));
    }

    /**
     * Get the user info for the authenticated User
     * @return The authenticated user
     */
    public User user() {
        return requestBuilder.get("/user/info", null).getUser();
    }

    /**
     * Get the user dashboard for the authenticated User
     * @param options the options for the call (or null)
     * @return A List of posts
     */
    public List<Post> userDashboard(Map<String, ?> options) {
        return requestBuilder.get("/user/dashboard", options).getPosts();
    }

    public List<Post> userDashboard() {
        return this.userDashboard(null);
    }

    /**
     * Get the blogs the given user is following
     * @return a List of blogs
     */
    public List<Blog> userFollowing(Map<String, ?> options) {
        return requestBuilder.get("/user/following", options).getBlogs();
    }

    public List<Blog> userFollowing() { return this.userFollowing(null); }

    /**
     * Tagged posts
     * @param tag the tag to search
     * @param options the options for the call (or null)
     * @return a list of posts
     */
    public List<Post> tagged(String tag, Map<String, ?> options) {
        if (options == null) {
            options = Collections.emptyMap();
        }
        Map<String, Object> soptions = JumblrClient.safeOptionMap(options);
        soptions.put("api_key", apiKey);
        soptions.put("tag", tag);
        return requestBuilder.get("/tagged", soptions).getTaggedPosts();
    }

    public List<Post> tagged(String tag) {
        return this.tagged(tag, null);
    }

    /**
     * Get the blog info for a given blog
     * @param blogName the Name of the blog
     * @return The Blog object for this blog
     */
    public Blog blogInfo(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("api_key", this.apiKey);
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/info"), map).getBlog();
    }

    /**
     * Get the followers for a given blog
     * @param blogName the name of the blog
     * @return the blog object for this blog
     */
    public List<User> blogFollowers(String blogName, Map<String, ?> options) {
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/followers"), options).getUsers();
    }

    public List<User> blogFollowers(String blogName) { return this.blogFollowers(blogName, null); }

    /**
     * Get the public likes for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogLikes(String blogName, Map<String, ?> options) {
        if (options == null) {
            options = Collections.emptyMap();
        }
        Map<String, Object> soptions = JumblrClient.safeOptionMap(options);
        soptions.put("api_key", this.apiKey);
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/likes"), soptions).getLikedPosts();
    }

    public List<Post> blogLikes(String blogName) {
        return this.blogLikes(blogName, null);
    }

    /**
     * Get the posts for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogPosts(String blogName, Map<String, ?> options) {
        if (options == null) {
            options = Collections.emptyMap();
        }
        Map<String, Object> soptions = JumblrClient.safeOptionMap(options);
        soptions.put("api_key", apiKey);

        String path = "/posts";
        if (soptions.containsKey("type")) {
            path += "/" + soptions.get("type").toString();
            soptions.remove("type");
        }
        return requestBuilder.get(JumblrClient.blogPath(blogName, path), soptions).getPosts();
    }

    public List<Post> blogPosts(String blogName) {
        return this.blogPosts(blogName, null);
    }

    /**
     * Get an individual post by id
     * @param blogName the name of the blog
     * @param postId the id of the post to get
     * @return the Post or null
     */
    public Post blogPost(String blogName, Long postId) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("id", postId.toString());
        List<Post> posts = this.blogPosts(blogName, options);
        return posts.size() > 0 ? posts.get(0) : null;
    }

    /**
     * Get the queued posts for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogQueuedPosts(String blogName, Map<String, ?> options) {
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/posts/queue"), options).getPosts();
    }

    public List<Post> blogQueuedPosts(String blogName) {
        return this.blogQueuedPosts(blogName, null);
    }

    /**
     * Get the draft posts for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogDraftPosts(String blogName, Map<String, ?> options) {
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/posts/draft"), options).getPosts();
    }

    public List<Post> blogDraftPosts(String blogName) {
        return this.blogDraftPosts(blogName, null);
    }

    /**
     * Get the submissions for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogSubmissions(String blogName, Map<String, ?> options) {
        return requestBuilder.get(JumblrClient.blogPath(blogName, "/posts/submission"), options).getPosts();
    }

    public List<Post> blogSubmissions(String blogName) {
        return this.blogSubmissions(blogName, null);
    }

    /**
     * Get the likes for the authenticated user
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> userLikes(Map<String, ?> options) {
        return requestBuilder.get("/user/likes", options).getLikedPosts();
    }

    public List<Post> userLikes() {
        return this.userLikes(null);
    }

    /**
     * Get a specific size avatar for a given blog
     * @param blogName the avatar URL of the blog
     * @param size The size requested
     * @return a string representing the URL of the avatar
     */
    public String blogAvatar(String blogName, Integer size) {
        String pathExt = size == null ? "" : "/" + size.toString();
        return requestBuilder.getRedirectUrl(JumblrClient.blogPath(blogName, "/avatar" + pathExt));
    }

    public String blogAvatar(String blogName) { return this.blogAvatar(blogName, null); }

    /**
     * Like a given post
     * @param postId the ID of the post to like
     * @param reblogKey The reblog key for the post
     */
    public void like(Long postId, String reblogKey) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        requestBuilder.post("/user/like", map);
    }

    /**
     * Unlike a given post
     * @param postId the ID of the post to unlike
     * @param reblogKey The reblog key for the post
     */
    public void unlike(Long postId, String reblogKey) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        requestBuilder.post("/user/unlike", map);
    }

    /**
     * Follow a given blog
     * @param blogName The name of the blog to follow
     */
    public void follow(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        requestBuilder.post("/user/follow", map);
    }

    /**
     * Unfollow a given blog
     * @param blogName the name of the blog to unfollow
     */
    public void unfollow(String blogName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        requestBuilder.post("/user/unfollow", map);
    }

    /**
     * Delete a given post
     * @param blogName the name of the blog the post is in
     * @param postId the id of the post to delete
     */
    public void postDelete(String blogName, Long postId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        requestBuilder.post(JumblrClient.blogPath(blogName, "/post/delete"), map);
    }

    /**
     * Reblog a given post
     * @param blogName the name of the blog to post to
     * @param postId the id of the post
     * @param reblogKey the reblog_key of the post
     * @param options Additional options (or null)
     */
    public Post postReblog(String blogName, Long postId, String reblogKey, Map<String, ?> options) {
        if (options == null) {
            options = new HashMap<String, String>();
        }
        Map<String, Object> soptions = JumblrClient.safeOptionMap(options);
        soptions.put("id", postId.toString());
        soptions.put("reblog_key", reblogKey);
        final Long reblogId = requestBuilder.post(JumblrClient.blogPath(blogName, "/post/reblog"), soptions).getId();
        return this.blogPost(blogName, reblogId);
    }

    public Post postReblog(String blogName, Long postId, String reblogKey) {
        return this.postReblog(blogName, postId, reblogKey, null);
    }

    /**
     * Save edits for a given post
     * @param blogName The blog name of the post
     * @param id the Post id
     * @param detail The detail to save
     */
    public void postEdit(String blogName, Long id, Map<String, ?> detail) throws IOException {
        Map<String, Object> sdetail = JumblrClient.safeOptionMap(detail);
        sdetail.put("id", id);
        requestBuilder.postMultipart(JumblrClient.blogPath(blogName, "/post/edit"), sdetail);
    }

    /**
     * Create a post
     * @param blogName The blog name for the post
     * @param detail the detail to save
     */
    public Long postCreate(String blogName, Map<String, ?> detail) throws IOException {
        return requestBuilder.postMultipart(JumblrClient.blogPath(blogName, "/post"), detail).getId();
    }

    /**
     * Set up a new post of a given type
     * @param blogName the name of the blog for this post (or null)
     * @param klass the type of Post to instantiate
     * @return the new post with the client set
     */
    public <T extends Post> T newPost(String blogName, Class<T> klass) throws IllegalAccessException, InstantiationException {
        T post = klass.newInstance();
        post.setClient(this);
        post.setBlogName(blogName);
        return post;
    }

    /**
     **
     **
     */

    private static String blogPath(String blogName, String extPath) {
        return "/blog/" + blogUrl(blogName) + extPath;
    }

    private static String blogUrl(String blogName) {
        return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }

    public void setRequestBuilder(RequestBuilder builder) {
        this.requestBuilder = builder;
    }

    public RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    private static Map<String, Object> safeOptionMap(Map<String, ?> map) {
        Map<String, Object> mod = new HashMap<String, Object>();
        mod.putAll(map);
        return mod;
    }

}
