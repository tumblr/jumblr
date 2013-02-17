package com.tumblr.jumblr.responses;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import java.util.List;

public class ResponseWrapper {

    private SuccessResponse response;
    private JumblrClient client;
    
    public User getUser() {
        User user = response.getUser();
        user.setClient(client);
        return user;
    }

    public Blog getBlog() {
        Blog blog = response.getBlog();
        blog.setClient(client);
        return blog;
    }

    public void setClient(JumblrClient client) {
        this.client = client;
    }

    public List<Post> getPosts() {
        List<Post> posts = response.getPosts();
        for (Post post : posts) {
            post.setClient(client);
        }
        return posts;
    }

    public List<User> getUsers() {
        List<User> users = response.getUsers();
        for (User user : users) {
            user.setClient(client);
        }
        return users;
    }

    public List<Post> getLikedPosts() {
        List<Post> posts = response.getLikedPosts();
        for (Post post : posts) { post.setClient(client); }
        return posts;
    }

    public List<Blog> getBlogs() {
        List<Blog> blogs = response.getBlogs();
        for (Blog blog : blogs) {
            blog.setClient(client);
        }
        return blogs;
    }

}
