package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.math.BigInteger;

public class Post {
  
    private BigInteger id;

    private JumblrClient client;
    
    public BigInteger getId() {
        return id;
    }
    
    public void setClient(JumblrClient client) {
        this.client = client;
    }
    
}
