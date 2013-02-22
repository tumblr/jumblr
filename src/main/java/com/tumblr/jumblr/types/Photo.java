package com.tumblr.jumblr.types;

import java.util.List;

/**
 * This class represents a Photo in a PhotoPost
 * @author jc
 */
public class Photo {

    private String caption;
    private List<PhotoSize> alt_sizes;

    /**
     * Get the sizes of this Photo
     * @return PhotoSize[] sizes
     */
    public List<PhotoSize> getSizes() {
        return alt_sizes;
    }

    /**
     * Get the caption of this photo
     * @return the caption
     */
    public String getCaption() {
        return this.caption;
    }

}
