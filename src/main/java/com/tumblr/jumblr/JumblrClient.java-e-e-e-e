package com.tumblr.jumblr;

import com.google.gson.Gson;
import com.tumblr.jumblr.responses.ResponseWrapper;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import java.net.HttpURLConnection;
import java.util.HashMap;
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
     * @return a collection of blogs
     */
    public Iterable<Blog> userFollowing(Map options) {
        return this.clearGet("/user/following", options).getBlogs();
    }

    public Iterable<Blog> userFollowing() { return this.userFollowing(null); }
    
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
    public Iterable<User> blogFollowers(String blogName, Map<String, String> options) {
        return this.clearGet(JumblrClient.blogPath(blogName, "/followers"), options).getUsers();
    }

    public Iterable<User> blogFollowers(String blogName) { return this.blogFollowers(blogName, null); }
    
    /**
     * Get the public likes for a given blog
     * @
     */
    public Iterable<Post> blogLikes(String blogName, Map<String, String> options) {
        if (options == null) {
            options = new HashMap<String, String>();
        }
        options.put("api_key", this.apiKey);
        return this.clearGet(JumblrClient.blogPath(blogName, "/likes"), options).getLikedPosts();
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
     **
     **
     */
    
    private ResponseWrapper clearGet(String path) {
        return this.clearGet(path, null);
    }
    
    private ResponseWrapper clearGet(String path, Map<String, String> map) {
        Response response = this.get(path, map);
        if (response.getCode() == 200) {
            String json = response.getBody();
            Gson gson = new Gson();
            ResponseWrapper wrapper = gson.fromJson(json, ResponseWrapper.class);
            wrapper.setClient(this);
            return wrapper;
        } else {
            System.out.println(path);
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

    private static String blogPath(String blogName, String extPath) {
        String bn = blogName.contains(".") ? blogName : blogName + ".tumblr.com";
        return "/blog/" + bn + extPath;
    }

}
