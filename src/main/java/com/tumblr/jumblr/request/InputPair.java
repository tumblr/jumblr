package com.tumblr.jumblr.request;

import java.io.InputStream;

/**
 * A object used to wrap an InputStream and mime type
 * @author john
 */
public class InputPair {

    private final InputStream stream;
    private final String mime;
    private final long length;
    private final String name;

    public InputPair(InputStream stream, long length, String mime, String name) {
        this.stream = stream;
        this.length = length;
        this.mime = mime;
        this.name = name;
    }

    public InputPair(InputStream stream, long length, String mime) {
        this(stream, length, mime, "untitled");
    }

    String getMime() {
        return mime;
    }

    long getLength() {
        return this.length;
    }

    InputStream getStream() {
        return this.stream;
    }

    String getName() {
        return this.name;
    }

}
