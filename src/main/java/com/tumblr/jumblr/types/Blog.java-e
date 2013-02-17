package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.util.List;
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
    public String avatar() {
        return client.blogAvatar(this.name);
    }
    
    /**
     * Get the avatar for this blog (of a given size
     * @param size the size to get the avatar for
     * @return A string URL for the avatar
     */
    public String avatar(int size) {
        return client.blogAvatar(this.name, size);
    }

    /**
     * Get followers for this blog
     * @param options a map of options (or null)
     * @return A List of users
     */
    public List<User> followers(Map options) {
        return client.blogFollowers(this.name, options);
    }
    
    public List<User> followers() { return this.followers(null); }
    
    /**
     * Get likes posts for this blog
     * @param options a map of options (or null)
     * @return A List of posts
     */
    public List<Post> likedPosts(Map options) {
        return client.blogLikes(this.name, options);
    }
    
    public List<Post> likedPosts() { return this.likedPosts(null); }

    /**
     * Follow this blog
     */
    public void follow() {
        client.follow(this.name);
    }
    
    /**
     * Unfollow this blog
     */
    public void unfollow() {
        client.unfollow(this.name);
    }

    /**
     * Get the queued posts for this blog
     * @param options the options (or null)
     * @return a List of posts
     */
    public List<Post> queuedPosts(Map options) {
        return client.blogQueuedPosts(name, options);
    }

    public List<Post> queuedPosts() {
        return this.queuedPosts(null);
    }    
    
    /**
     * Get the draft posts for this blog
     * @param options the options (or null)
     * @return a List of posts
     */
    public List<Post> draftPosts(Map options) {
        return client.blogDraftPosts(name, options);
    }
    
    public List<Post> draftPosts() {
        return this.draftPosts(null);
    }
    
    /**
     * Get the submissions for this blog
     * @param options the options (or null)
     * @return a List of posts
     */
    public List<Post> submissions(Map options) {
        return client.blogSubmissions(name, options);
    }
    
    public List<Post> submissions() {
        return this.submissions(null);
    }

}
