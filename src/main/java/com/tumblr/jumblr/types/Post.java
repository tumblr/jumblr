package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.math.BigInteger;
import java.util.Map;

public class Post {
  
    private BigInteger id;
    private String reblog_key;
    private String blog_name;
    
    private JumblrClient client;
    
    public BigInteger getId() {
        return id;
    }
    
    public void setClient(JumblrClient client) {
        this.client = client;
    }

    public String getReblogKey() {
        return this.reblog_key;
    }

    /**
     * Delete this post
     */
    public void delete() {
        client.postDelete(blog_name, id);
    }
    
    /**
     * Reblog this post
     * @param blogName the blog name to reblog onto
     * @param options options to reblog with (or null)
     * @return reblogged post
     */
    public Post reblog(String blogName, Map<String, ?> options) {
        return client.postReblog(blogName, id, reblog_key, options);
    }
    
    public Post reblog(String blogName) {
        return this.reblog(blogName, null);
    }
    
    /**
     * Like this post
     */
    public void like() {
        client.like(this.id, this.reblog_key);
    }
    
    /**
     * Unlike this post
     */
    public void unlike() {
        client.unlike(this.id, this.reblog_key);
    }
    
}
