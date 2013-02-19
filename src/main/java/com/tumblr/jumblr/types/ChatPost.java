package com.tumblr.jumblr.types;

public class ChatPost extends Post {

    // @TODO make editable

    private String title;
    private String body;
    private Dialogue[] dialogue;

    public Dialogue[] getDialogue() {
        return dialogue;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

}
