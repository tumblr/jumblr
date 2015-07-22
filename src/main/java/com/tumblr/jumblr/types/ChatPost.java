package com.tumblr.jumblr.types;

import java.util.List;
import java.util.Map;

/**
 * This class represents a post of type "chat"
 * @author jc
 */
public class ChatPost extends SafePost {

    private String title;
    private String body;
    private List<Dialogue> dialogue;

    /**
     * Get the dialogues for this post
     * @return an Array[Dialogue]
     */
    public List<Dialogue> getDialogue() {
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
     * Set the title for this post
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public PostType getType() {
        return PostType.CHAT;
    }

    /**
     * Get the details for this post (and the base details)
     * @return the detail
     */
    @Override
    public Map<String, Object> detail() {
        final Map<String, Object> details = super.detail();
        details.put("title", title);
        details.put("conversation", body);
        return details;
    }

}
