package com.tumblr.jumblr.responses;

import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import java.util.List;

class SuccessResponse {

    private Long id;
    
    private User user;
    private Blog blog;
    private Post post;

    private List<User> users;
    private List<Post> posts;
    private List<Post> liked_posts;
    private List<Blog> blogs;

    User getUser() {
        return this.user;
    }

    Blog getBlog() {
        return this.blog;
    }

    List<Post> getPosts() {
        return this.posts;
    }

    List<User> getUsers() {
        return this.users;
    }

    List<Post> getLikedPosts() {
        return this.liked_posts;
    }

    List<Blog> getBlogs() {
        return this.blogs;
    }

    Post getPost() {
        return this.post;
    }

    Long getId() {
        return id;
    }

}
