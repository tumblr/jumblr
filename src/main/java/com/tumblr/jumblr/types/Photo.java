package com.tumblr.jumblr.types;

import java.io.File;
import java.util.List;

/**
 * This class represents a Photo in a PhotoPost
 * @author jc
 */
public class Photo {

    /**
     * Types of the post - what kind of data does it have?
     */
    public enum PhotoType {
        SOURCE("source"),
        FILE("data");

        private final String prefix;
        private PhotoType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    };

    private String caption;
    private List<PhotoSize> alt_sizes;
    private PhotoSize original_size;

    private String source;
    private File file;

    /**
     * Create a new photo with a data
     * @param file the file for the photo
     */
    public Photo(File file) {
        this.file = file;
    }

    /**
     * Create a new photo with a source
     * @param source the source for the photo
     */
    public Photo(String source) {
        this.source = source;
    }

    /**
     * Get the type of this photo
     * @return PhotoType the type of photo
     */
    public PhotoType getType() {
        if (this.source != null) { return PhotoType.SOURCE; }
        if (this.file   != null) { return PhotoType.FILE;   }
        return null;
    }

    /**
     * Get the sizes of this Photo
     * @return PhotoSize[] sizes
     */
    public List<PhotoSize> getSizes() {
        return alt_sizes;
    }

    /**
     * Get the original sized photo
     * @return the original sized PhotoSize
     */
    public PhotoSize getOriginalSize() {
        return original_size;
    }

    /**
     * Get the caption of this photo
     * @return the caption
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Get the detail for this photo
     * @return the detail (String or File)
     */
    protected Object getDetail() {
        if (this.source != null) {
            return source;
        } else if (this.file != null) {
            return file;
        } else {
            return null;
        }
    }

}
