package com.tumblr.jumblr.types;

public class PhotoPost extends Post {

    // @TODO make editable

    private String caption;
    private int width, height;

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

}
