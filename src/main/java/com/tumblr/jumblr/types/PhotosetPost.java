package com.tumblr.jumblr.types;

import java.io.File;
import java.util.ArrayList;

/**
 * A post that is a set of photos
 * @author jc
 */
public class PhotosetPost extends PhotoPost {

    /**
     * Add a photo to this post (multiple allowed) for Photoset
     * @param photo the photo to add
     * @throws IllegalArgumentException photo type clash
     */
    public void addPhoto(Photo photo) {
        Photo.PhotoType type = photo.getType();
        if (postType != null && !postType.equals(type)) {
            throw new IllegalArgumentException("Photos must all be the same type (source or data)");
        }
        if (pendingPhotos == null) {
            pendingPhotos = new ArrayList<Photo>();
        }
        pendingPhotos.add(photo);
        this.postType = type;
    }

    /**
     * Add an image source to this post (multiple allowed)
     * @param source the source
     * @throws IllegalArgumentException data is already set
     */
    public void addSource(String source) {
        addPhoto(new Photo(source));
    }

        /**
     * Add an image to this post (multiple allowed)
     * @param file the file to read from
     * @throws IllegalArgumentException source is already set
     */
    public void addData(File file) {
        addPhoto(new Photo(file));
    }


}
