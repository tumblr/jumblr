package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "audio"
 * @author jc
 */
public class AudioPost extends Post {

    // @TODO make data editable

    private String caption, player;
    private int plays;
    private String album_art, artist, album, track_name;
    private int track_number, year;

    private String external_url;
    private String data;

    /**
     * Get the play count for this post
     * @return the play count
     */
    public int getPlayCount() {
        return plays;
    }

    /**
     * Get the track name for this post
     * @return the track name
     */
    public String getTrackName() {
        return track_name;
    }

    /**
     * Get the album for this post
     * @return the album name
     */
    public String getAlbumName() {
        return album;
    }

    /**
     * Get the artist for this post
     * @return the artist
     */
    public String getArtistName() {
        return artist;
    }

    /**
     * Get the album art for this post
     * @return the album art
     */
    public String getAlbumArtUrl() {
        return album_art;
    }

    /**
     * Get the year for this post
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Get the track number for this post
     * @return the track number
     */
    public int getTrackNumber() {
        return track_number;
    }

    /**
     * Get the caption for this post
     * @return the caption
     */
    public String getCaption () {
        return caption;
    }

    /**
     * Get the embed code for this Post
     * @return the embed code
     */
    public String getEmbedCode() {
        return player;
    }

    /**
     * Set the external url for this post
     * @param url the external url
     * @throws IllegalArgumentException when data is already set
     */
    public void setExternalUrl(String url) {
        if (this.data != null) {
            throw new IllegalArgumentException("Cannot provide both data & external_url");
        }
        this.external_url = url;
    }

    /**
     * Set the caption for this post
     * @param caption the caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Get the details about this post (along with base details)
     * @return the detail
     */
    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("type", "audio");
        details.put("caption", caption);
        details.put("external_url", external_url);
        return details;
    }

}