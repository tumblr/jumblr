package com.tumblr.jumblr.types;

import java.util.Map;

public class ChatPost extends Post {

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

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("title", title);
        details.put("conversation", body);
        details.put("type", "chat");
        return details;
    }

}
