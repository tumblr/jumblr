package com.tumblr.jumblr.types;

import java.util.Map;

public class AudioPost extends Post {

    // @TODO make data editable

    private String caption, player;
    private int plays;
    private String album_art, artist, album, track_name;
    private int track_number, year;

    private String external_url;
    private String data;

    public int getPlays() {
        return plays;
    }

    public String getTrackName() {
        return track_name;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArt() {
        return album_art;
    }

    public int getYear() {
        return year;
    }

    public int getTrackNumber() {
        return track_number;
    }

    public String getCaption () {
        return caption;
    }

    public String getEmbedCode() {
        return player;
    }

    public void setExternalUrl(String url) {
        if (this.data != null) {
            throw new IllegalArgumentException("Cannot provide both data & external_url");
        }
        this.external_url = url;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("type", "audio");
        details.put("caption", caption);
        details.put("external_url", external_url);
        return details;
    }

}