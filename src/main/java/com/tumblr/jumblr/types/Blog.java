package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.util.Map;

public class Blog {

    private String name;
    private String title;
    
    private JumblrClient client;
    
    /**
     * Set the client for making relative requests from this Blog
     * @param client The client to use
     */
    public void setClient(JumblrClient client) {
        this.client = client;
    }
    
    /**
     * Get the title of this blog
     * @return The title of the blog
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Get the name of this blog
     * @return The name of the blog
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the avatar for this blog
     * @return A String URL for the avatar
     */
    public String getAvatar() {
        return client.blogAvatar(this.name);
    }
    
    /**
     * Get the avatar for this blog (of a given size
     * @param size the size to get the avatar for
     * @return A string URL for the avatar
     */
    public String getAvatar(int size) {
        return client.blogAvatar(this.name, size);
    }

    /**
     * Get followers for this blog
     * @param options a map of options (or null)
     * @return A collection of users
     */
    public Iterable<User> getFollowers(Map options) {
        return client.blogFollowers(this.name, options);
    }
    
    public Iterable<User> getFollowers() { return this.getFollowers(null); }
    
    /**
     * Get likes posts for this blog
     * @param options a map of options (or null)
     * @return A collection of posts
     */
    public Iterable<Post> getLikedPosts(Map options) {
        return client.blogLikes(this.name, options);
    }
    
    public Iterable<Post> getLikedPosts() { return this.getLikedPosts(null); }
    
    
}
