package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "chat"
 * @author jc
 */
public class ChatPost extends Post {

    private String title;
    private String body;
    private Dialogue[] dialogue;

    /**
     * Get the dialogues for this post
     * @return an Array[Dialogue]
     */
    public Dialogue[] getDialogue() {
        return dialogue;
    }

    /**
     * Get the title for this post
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the body for this post
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the body for this post
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get the details for this post (and the base details)
     * @return the detail
     */
    @Override
    public Map<String, String> detail() {
        Map<String, String> details = super.detail();
        details.put("title", title);
        details.put("conversation", body);
        details.put("type", "chat");
        return details;
    }

}
