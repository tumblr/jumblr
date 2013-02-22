package com.tumblr.jumblr.types;

/**
 * An individual Video in a VideoPost
 * @author jc
 */
public class Video {

    private Integer width;
    private String embed_code;

    /**
     * Get the width of this video
     * @return width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Get the embed code for this video
     * @return embed code
     */
    public String getEmbedCode() {
        return embed_code;
    }

}
