package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.util.List;

public class User {
    
    private List<Blog> blogs;
    private String name;
    private int following;
    
    private JumblrClient client;
    
    /**
     * Get the name for this User object
     * @return The name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get the number of users this user is following
     * @return The following count
     */
    public int getFollowing() {
        return this.following;
    }

    /**
     * Get the blog List for this user
     * @return The blog List for this user
     */
    public List<Blog> getBlogs() {
        for (Blog blog : blogs) {
            blog.setClient(client);
        }
        return this.blogs;
    }

    /**
     * Set the client for this User
     * @param client The client to use for relative requests
     */
    public void setClient(JumblrClient client) {
        this.client = client;
    }

}
