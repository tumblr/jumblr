package com.tumblr.jumblr.types;

public class LinkPost extends Post {
    
    private String title;
    private String url;
    private String description;
    
    public String getTitle() {
        return title;
    }
    
    public String getLinkUrl() {
        return url;
    }
    
    public String description() {
        return description;
    }
    
}
