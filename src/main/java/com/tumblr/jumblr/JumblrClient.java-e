package com.tumblr.jumblr;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tumblr.jumblr.responses.ResponseWrapper;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public final class JumblrClient {

    private final OAuthService service;
    private Token token = null;
    private String apiKey;

    /**
     * Instantiate a new Jumblr Client with no token
     * @param consumerKey The consumer key for the client
     * @param consumerSecret The consumer secret for the client
     */
    public JumblrClient(String consumerKey, String consumerSecret) {
        this.apiKey = consumerKey;
        this.service = new ServiceBuilder().
            provider(TumblrApi.class).
            apiKey(consumerKey).apiSecret(consumerSecret).
            build();        
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
        this.token = new Token(token, tokenSecret);
    }
    
    /**
     * Get the user info for the authenticated User
     * @return The authenticated user
     */
    public User userInfo() {
        return this.clearGet("/user/info").getUser();
    }
    
    /**
     * Get the blogs the given user is following
     * @return a List of blogs
     */
    public List<Blog> userFollowing(Map options) {
        return this.clearGet("/user/following", options).getBlogs();
    }

    public List<Blog> userFollowing() { return this.userFollowing(null); }
    
    /**
     * Get the blog info for a given blog
     * @param blogName the Name of the blog
     * @return The Blog object for this blog
     */
    public Blog blogInfo(String blogName) {
        HashMap map = new HashMap<String, String>();
        map.put("api_key", this.apiKey);
        return this.clearGet(JumblrClient.blogPath(blogName, "/info"), map).getBlog();
    }
    
    /**
     * Get the followers for a given blog
     * @param blogName the name of the blog
     * @return the blog object for this blog
     */
    public List<User> blogFollowers(String blogName, Map<String, String> options) {
        return this.clearGet(JumblrClient.blogPath(blogName, "/followers"), options).getUsers();
    }

    public List<User> blogFollowers(String blogName) { return this.blogFollowers(blogName, null); }
    
    /**
     * Get the public likes for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogLikes(String blogName, Map<String, String> options) {
        if (options == null) {
            options = new HashMap<String, String>();
        }
        options.put("api_key", this.apiKey);
        return this.clearGet(JumblrClient.blogPath(blogName, "/likes"), options).getLikedPosts();
    }
    
    public List<Post> blogLikes(String blogName) {
        return this.blogLikes(blogName, null);
    }
    
    /**
     * Get the queued posts for a given blog
     * @param blogName the name of the blog
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> blogQueuedPosts(String blogName, Map<String, String> options) {
        return this.clearGet(JumblrClient.blogPath(blogName, "/posts/queue"), options).getPosts();
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
    public List<Post> blogDraftPosts(String blogName, Map<String, String> options) {
        return this.clearGet(JumblrClient.blogPath(blogName, "/posts/draft"), options).getPosts();
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
    public List<Post> blogSubmissions(String blogName, Map<String, String> options) {
        return this.clearGet(JumblrClient.blogPath(blogName, "/posts/submission"), options).getPosts();
    }
    
    public List<Post> blogSubmissions(String blogName) {
        return this.blogSubmissions(blogName, null);
    }    
    
    /**
     * Get the likes for the authenticated user
     * @param options the options for this call (or null)
     * @return a List of posts
     */
    public List<Post> userLikes(Map<String, String> options) {
        return this.clearGet("/user/likes", options).getLikedPosts();
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
        boolean presetVal = HttpURLConnection.getFollowRedirects();
        HttpURLConnection.setFollowRedirects(false);
        Response response = this.get(JumblrClient.blogPath(blogName, "/avatar" + pathExt));
        HttpURLConnection.setFollowRedirects(presetVal);
        if (response.getCode() == 301) {
            return response.getHeader("Location");
        } else {
            return null; // @TODO
        }        
    }

    public String blogAvatar(String blogName) { return this.blogAvatar(blogName, null); }
 
    /**
     * Like a given post
     * @param postId the ID of the post to like
     * @param reblogKey The reblog key for the post
     */
    public void like(BigInteger postId, String reblogKey) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        this.clearPost("/user/like", map);
    }
    
    /**
     * Unlike a given post
     * @param postId the ID of the post to unlike
     * @param reblogKey The reblog key for the post
     */
    public void unlike(BigInteger postId, String reblogKey) {
        Map map = new HashMap<String, String>();
        map.put("id", postId.toString());
        map.put("reblog_key", reblogKey);
        this.clearPost("/user/unlike", map);
    }
    
    /**
     * Follow a given blog
     * @param blogName The name of the blog to follow
     */
    public void follow(String blogName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        this.clearPost("/user/follow", map);
    }
    
    /**
     * Unfollow a given blog
     * @param blogName the name of the blog to unfollow
     */
    public void unfollow(String blogName) {
        Map map = new HashMap<String, String>();
        map.put("url", JumblrClient.blogUrl(blogName));
        this.clearPost("/user/follow", map);
    }
    
    /**
     **
     **
     */
    
    private ResponseWrapper clearGet(String path) {
        return this.clearGet(path, null);
    }
    
    private ResponseWrapper clearPost(String path, Map<String, String> bodyMap) {
        Response response = this.post(path, bodyMap);
        System.out.println(response.getBody());
        return this.clear(response);
    }
    
    private ResponseWrapper clearGet(String path, Map<String, String> map) {
        Response response = this.get(path, map);
        return this.clear(response);
    }
    
    private ResponseWrapper clear(Response response) {
        if (response.getCode() == 200) {
            String json = response.getBody();
            System.out.println(json);
            try {
                Gson gson = new Gson();
                ResponseWrapper wrapper = gson.fromJson(json, ResponseWrapper.class);
                wrapper.setClient(this);
                return wrapper;
            } catch (JsonSyntaxException ex) {
                return null;
            }
        } else {
            System.out.println(response.getCode());
            return null; // @TODO
        }
    }
    
    private Response get(String path) {
        return this.get(path, null);
    }
    
    private Response get(String path, Map<String, String> queryParams) {
        String url = "http://api.tumblr.com/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        if (queryParams != null) {
            for (String key : queryParams.keySet()) {
                request.addQuerystringParameter(key, queryParams.get(key));
            }
        }
        service.signRequest(token, request);
        return request.send();
    }

    private Response post(String path, Map<String, String> bodyMap) {
        String url = "http://api.tumblr.com/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        if (bodyMap != null) {
            for (String key : bodyMap.keySet()) {
                request.addBodyParameter(key, bodyMap.get(key));
            }
        }
        service.signRequest(token, request);
        return request.send();
    }
    
    private static String blogPath(String blogName, String extPath) {
        String bn = blogName.contains(".") ? blogName : blogName + ".tumblr.com";
        return "/blog/" + bn + extPath;
    }

    private static String blogUrl(String blogName) {
        return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }

}
