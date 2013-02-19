package com.tumblr.jumblr.types;

import java.util.Map;

public class VideoPost extends Post {

    // @TODO make data editable

    private String caption;
    private Video[] player;

    private String data, embed;

    public Video[] getVideos() {
        return this.player;
    }

    public String getCaption() {
        return caption;
    }

    public void setEmbedCode(String embed) {
        if (data != null) {
            throw new IllegalArgumentException("Cannot supply both data & embed");
        }
        this.embed = embed;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("caption", caption);
        details.put("embed", embed);
        details.put("type", "video");
        return details;
    }

}
