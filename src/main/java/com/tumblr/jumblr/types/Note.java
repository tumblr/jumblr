package com.tumblr.jumblr.types;

public class Note {
    
    private Long timestamp;
    private String blog_name;
    private String blog_url;
    private String type;
    private Long post_id;
    private String reply_text;
    private String added_text;

    /**
     * Get the timestamp of this note
     *
     * @return timestamp since the epoch
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the blog name
     *
     * @return the blog name for the note
     */
    public String getBlogName() {
        return blog_name;
    }

    /**
     * Get the blog URL
     *
     * @return the blog URL for the note
     */
    public String getBlogUrl() {
        return blog_url;
    }

    /**
     * Get the type of the note. This is either "like" or "reblog"
     *
     * @return the type of the note; either "like" or "reblog"
     */
    public String getType() {
        return type;
    }

    /**
     * Get the ID of the reblog. This only exists if this is a reblog; otherwise
     * this returns null.
     *
     * @return the ID of the post
     */
    public Long getPostId() {
        return post_id;
    }

    /**
     * Returns the added text of the reblog. This only exists if this is a reblog; otherwise
     * this returns null.
     *
     * @return the added text of the reblog.
     */
    public String getAddedText() {
        return added_text;
    }

    /**
     * Returns the reply text of the reply. This only exists if this is a reply; otherwise
     * this returns null.
     *
     * @return the reply text of the reply.
     */
    public String getReplyText() {
        return reply_text;
    }

}
