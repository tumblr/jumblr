package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a Post of type "link"
 * @author jc
 */
public class LinkPost extends Post {

    // @TODO comment

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLinkUrl(String url) {
        this.url = url;
    }

    @Override
    public Map<String, String> detail() {
        Map<String, String> detail = super.detail();
        detail.put("title", title);
        detail.put("url", url);
        detail.put("description", description);
        detail.put("type", "link");
        return detail;
    }

}
