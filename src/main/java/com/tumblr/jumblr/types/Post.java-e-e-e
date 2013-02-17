package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.math.BigInteger;

public class Post {
  
    private BigInteger id;
    private String reblog_key;
    
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
