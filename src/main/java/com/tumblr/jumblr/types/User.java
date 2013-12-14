package com.tumblr.jumblr.types;

import java.util.List;

/**
 * This class represents a User on Tumblr
 * @author jc
 */
public class User extends Resource {

    private List<Blog> blogs;
    private String name;
    private Integer following, likes;
    private String default_post_format;

    /**
     * Return the default post format for this user
     * @return String format
     */
    public String getDefaultPostFormat() {
        return default_post_format;
    }

    /**
     * Get the name for this User object
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the number of likes for this user
     * @return the likes count
     */
    public Integer getLikeCount() {
        return this.likes;
    }

    /**
     * Get the number of users this user is following
     * @return The following count
     */
    public Integer getFollowingCount() {
        return this.following;
    }

    /**
     * Get the blog List for this user
     * @return The blog List for this user
     */
    public List<Blog> getBlogs() {
        if (blogs != null) {
            for (Blog blog : blogs) {
                blog.setClient(client);
            }
        }
        return this.blogs;
    }

}
