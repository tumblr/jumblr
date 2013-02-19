package com.tumblr.jumblr.types;

public class AudioPost extends Post {

    // @TODO make editable

    private String caption, player;
    private int plays;
    private String album_art, artist, album, track_name;
    private int track_number, year;

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

}