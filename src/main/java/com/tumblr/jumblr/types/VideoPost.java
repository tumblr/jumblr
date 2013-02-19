package com.tumblr.jumblr.types;

public class VideoPost extends Post {

    // @TODO make editable

    private String caption;
    private Video[] player;

    public Video[] getVideos() {
        return this.player;
    }

}
