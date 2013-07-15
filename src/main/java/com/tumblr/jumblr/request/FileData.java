package com.tumblr.jumblr.request;

/**
 * A class for data/mime
 * @author jc
 */
public class FileData {

    private byte[] data;
    private String mime;
    private String name;

    public FileData(byte[] data, String mime, String name) {
        this.data = data;
        this.mime = mime;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMime() {
        return mime;
    }

    public byte[] getData() {
        return data;
    }

}
