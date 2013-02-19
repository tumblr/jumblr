package com.tumblr.jumblr.types;

import java.util.Map;

public class QuotePost extends Post {

    private String text;
    private String source;

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    protected Map<String, String> detail() {
        Map<String, String> map = super.detail();
        map.put("quote", this.text);
        map.put("source", this.source);
        map.put("type", "quote");
        return map;
    }

}
