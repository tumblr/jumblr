package com.tumblr.jumblr.types;

/**
 *
 * @author jc
 */
public class Photo {

    private String caption;
    private PhotoSize[] alt_sizes;

    public PhotoSize[] getSizes() {
        return alt_sizes;
    }

    public String getCaption() {
        return this.caption;
    }

}
