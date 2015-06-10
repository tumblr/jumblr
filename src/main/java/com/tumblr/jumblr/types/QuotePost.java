package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * This class represents a post of type "quote"
 * @author jc
 */
public class QuotePost extends SafePost {

    private String text;
    private String source;

    /**
     * Get the text of this post
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the source of this post
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Set the text of this post
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the source of this post
     * @param source the source
     */
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public PostType getType() {
        return PostType.QUOTE;
    }

    /**
     * Get the details for this post (and the base details)
     * @return the details
     */
    @Override
    protected Map<String, Object> detail() {
        final Map<String, Object> map = super.detail();
        map.put("quote", this.text);
        map.put("source", this.source);
        return map;
    }

}
