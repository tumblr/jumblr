package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "text"
 * @author jc
 */
public class TextPost extends SafePost {

    private String title;
    private String body;

    /**
     * Get the title of this post
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the body of this post
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the title of this post
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the body of this post
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get the details of this post (and the base details)
     * @return the details
     */
    @Override
    protected Map<String, Object> detail() {
        Map<String, Object> map = super.detail();
        map.put("title", this.title);
        map.put("body", this.body);
        map.put("type", "text");
        return map;
    }

}
