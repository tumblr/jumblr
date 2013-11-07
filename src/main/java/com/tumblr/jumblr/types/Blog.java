package com.tumblr.jumblr.types;

import java.util.List;
import java.util.Map;

/**
 * This class represents an individual Tumbelog
 * @author jc
 */
public class Blog extends Resource {

    private String name;
    private String title;
    private String description;
    private int posts, likes, followers;
    private Long updated;
    private boolean ask, ask_anon;

    /**
     * Get the description of this blog
     * @return String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Can we ask questions on this blog?
     * @return boolean
     */
    public boolean canAsk() {
        return this.ask;
    }

    /**
     * Can we ask questions on this blog anonymously?
     * @return boolean
     */
    public boolean canAskAnonymously() {
        return this.ask_anon;
    }

    /**
     * Get the number of posts for this blog
     * @return int the number of posts
     */
    public Integer getPostCount() {
        return this.posts;
    }

    /**
     * Get the number of likes for this blog
     * @return int the number of likes
     */
    public Integer getLikeCount() {
        return this.likes;
    }

    /**
     * Get the time of the most recent post (in seconds since epoch)
     * @return Long of time
     */
    public Long getUpdated() {
        return updated;
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
     * Get the avatar for this blog (of a given size
     * @param size the size to get the avatar for
     * @return A string URL for the avatar
     */
    public String avatar(Integer size) {
        return client.blogAvatar(this.name, size);
    }

    public String avatar() {
        return this.avatar(null);
    }

    public Integer getFollowersCount() {
        return this.followers;
    }

    /**
     * Get followers for this blog
     * @param options a map of options (or null)
     * @return A List of users
     */
    public List<User> followers(Map<String, ?> options) {
        return client.blogFollowers(this.name, options);
    }

    public List<User> followers() { return this.followers(null); }

    /**
     * Get the posts for this blog
     * @param options a map of options (or null)
     * @return A List of posts
     */
    public List<Post> posts(Map<String, ?> options) {
        return client.blogPosts(name, options);
    }

    public List<Post> posts() {
        return this.posts(null);
    }

    /**
     * Get an individual post by id
     * @param postId the id of the post to retrieve
     * @return the post (or null)
     */
    public Post getPost(Long postId) {
        return client.blogPost(name, postId);
    }

    /**
     * Get likes posts for this blog
     * @param options a map of options (or null)
     * @return A List of posts
     */
    public List<Post> likedPosts(Map<String, ?> options) {
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
    public List<Post> queuedPosts(Map<String, ?> options) {
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
    public List<Post> draftPosts(Map<String, ?> options) {
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
    public List<Post> submissions(Map<String, ?> options) {
        return client.blogSubmissions(name, options);
    }

    public List<Post> submissions() {
        return this.submissions(null);
    }

    /**
     * Create a new post of a given type for this blog
     * @param klass the class of the post to make
     * @return new post
     */
    public <T extends Post> T newPost(Class<T> klass) throws IllegalAccessException, InstantiationException {
        return client.newPost(name, klass);
    }

    /**
     * Set the name of this blog
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}