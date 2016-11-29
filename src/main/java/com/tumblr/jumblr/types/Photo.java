package com.tumblr.jumblr.types;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.tumblr.jumblr.request.InputPair;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLConnection;
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
    private InputPair inputPair;

    /**
     * Create a new photo with a data
     * @param file the file for the photo
     */
    public Photo(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        String mime = URLConnection.guessContentTypeFromName(file.getName());
        this.inputPair = new InputPair(fis, file.length(), mime, file.getName());
    }

    /**
     * Create a new photo with a source
     * @param source the source for the photo
     */
    public Photo(String source) {
        this.source = source;
    }

    /**
     * Create a new photo with an InputStream
     * @param stream the input stream
     * @param mime the mime type
     */
    public Photo(InputStream stream, long length, String mime) {
        this.inputPair = new InputPair(stream, length, mime);
    }

    /**
     * Create a new photo with a byte[]
     * @param array the byte array
     * @param mime the mime type
     */
    public Photo(byte[] arr, String mime) {
        ByteInputStream bis = new ByteInputStream(arr, arr.length);
        this.inputPair = new InputPair(bis, arr.length, mime);
    }

    /**
     * Get the type of this photo
     * @return PhotoType the type of photo
     */
    public PhotoType getType() {
        if (this.source != null) { return PhotoType.SOURCE; }
        if (this.inputPair != null) { return PhotoType.FILE;   }
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
        } else if (this.inputPair != null) {
            return inputPair;
        } else {
            return null;
        }
    }

}
