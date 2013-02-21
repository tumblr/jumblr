package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "photo"
 * @author jc
 */
public class PhotoPost extends Post {

    // @TODO make data editable

    private String caption;
    private int width, height;

    private String source;
    private String data;
    private String link;
    private Photo[] photos;

    /**
     * Get the Photo collection for this post
     * @return the photos
     */
    public Photo[] getPhotos() {
        return photos;
    }

    /**
     * Return if this is a photoset or not
     * @return boolean
     */
    public boolean isPhotoset() {
        return photos.length > 1;
    }

    /**
     * Return the caption for this post
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Return the photo width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the photo height
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the caption for this post
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Set the source for this post
     * @param source the source to set
     * @throws IllegalArgumentException data is already set
     */
    public void setSource(String source) {
        if (this.data != null) {
            throw new IllegalArgumentException("Cannot supply both data & source");
        }
        this.source = source;
    }

    /**
     * Set the
     */

    /**
     * Set the link URL for this post
     * @param linkUrl the link URL
     */
    public void setLinkUrl(String linkUrl) {
        this.link = linkUrl;
    }

    /**
     * Get the detail for this post (and the base detail)
     * @return the details
     */
    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("type", "photo");
        details.put("link", link);
        details.put("caption", caption);
        details.put("source", source);
        return details;
    }

}
