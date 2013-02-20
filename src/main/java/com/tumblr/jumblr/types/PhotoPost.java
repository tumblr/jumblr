package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "photo"
 * @author jc
 */
public class PhotoPost extends Post {

    // @TODO make data editable
    // @TODO comment

    private String caption;
    private int width, height;

    private String source;
    private String data;
    private String link;

    private Photo[] photos;

    public Photo[] getPhotos() {
        return photos;
    }

    public boolean isPhotoset() {
        return photos.length > 1;
    }

    public String getCaption() {
        return caption;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setSource(String source) {
        if (this.data != null) {
            throw new IllegalArgumentException("Cannot supply both data & source");
        }
        this.source = source;
    }

    public void setLinkUrl(String linkUrl) {
        this.link = linkUrl;
    }

    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("type", "photo");
        details.put("caption", caption);
        details.put("source", source);
        return details;
    }

}
