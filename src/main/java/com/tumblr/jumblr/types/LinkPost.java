package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a Post of type "link"
 * @author jc
 */
public class LinkPost extends SafePost {

    private String title;
    private String url;
    private String description;

    /**
     * Get the title for this post
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the link URL for this post
     * @return the link URL
     */
    public String getLinkUrl() {
        return url;
    }

    /**
     * Get the description for this post
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the title for this post
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the description for this post
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the link URL for this post
     * @param url the link URL
     */
    public void setLinkUrl(String url) {
        this.url = url;
    }

    @Override
    public PostType getType() {
        return PostType.LINK;
    }

    /**
     * Get the detail for this post (and the base detail)
     * @return the details
     */
    @Override
    public Map<String, Object> detail() {
        final Map<String, Object> detail = super.detail();
        detail.put("title", title);
        detail.put("url", url);
        detail.put("description", description);
        return detail;
    }

}
