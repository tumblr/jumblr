package com.tumblr.jumblr.types;

import com.tumblr.jumblr.types.Photo.PhotoType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a post of type "photo"
 * @author jc
 */
public class PhotoPost extends Post {

    private String caption;
    private Integer width, height;

    private String link;
    // TODO: Do not leak the photos member variable to world
    private List<Photo> photos;

    protected List<Photo> pendingPhotos;
    protected PhotoType postType = null;

    /**
     * Get the Photo collection for this post
     * @return the photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * Return if this is a photoset or not
     * @return boolean
     */
    public boolean isPhotoset() {
        return photos != null && photos.size() > 1;
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
    public Integer getWidth() {
        return width;
    }

    /**
     * Return the photo height
     * @return height
     */
    public Integer getHeight() {
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
     * Set the photo for this post
     * @param photo the photo to add
     */
    public void setPhoto(Photo photo) {
        PhotoType type = photo.getType();
        if (postType != null && !postType.equals(type)) {
            throw new IllegalArgumentException("Photos must all be the same type (source or data)");
        } else if (postType == PhotoType.SOURCE && pendingPhotos.size() > 0) {
            throw new IllegalArgumentException("Only one source URL can be provided");
        }
        pendingPhotos = new ArrayList<Photo>();
        pendingPhotos.add(photo);
        this.postType = type;
    }

    /**
     * Set the source for this post
     * @param source the source to set
     * @throws IllegalArgumentException data is already set
     */
    public void setSource(String source) {
        setPhoto(new Photo(source));
    }

    /**
     * Set the data for this post (single photo)
     * @param file the file to read from
     * @throws IllegalArgumentException source is already set
     */
    public void setData(File file) {
        setPhoto(new Photo(file));
    }

    /**
     * Set the link URL for this post
     * @param linkUrl the link URL
     */
    public void setLinkUrl(String linkUrl) {
        this.link = linkUrl;
    }

    @Override
    public PostType getType() {
        return PostType.PHOTO;
    }

    /**
     * Get the detail for this post (and the base detail)
     * @return the details
     */
    @Override
    public Map<String, Object> detail() {
        final Map<String, Object> details = super.detail();
        details.put("link", link);
        details.put("caption", caption);

        if (pendingPhotos != null && pendingPhotos.size() > 0) {
            PhotoType type = pendingPhotos.get(0).getType();
            if (type == PhotoType.SOURCE) {
                details.put(type.getPrefix(), pendingPhotos.get(0).getDetail());
            } else if (type == PhotoType.FILE) {
                for (int i = 0; i < pendingPhotos.size(); i++) {
                    details.put(type.getPrefix() + "[" + i + "]", pendingPhotos.get(i).getDetail());
                }
            }
        }

        return details;
    }

}
