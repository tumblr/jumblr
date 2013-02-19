package com.tumblr.jumblr.types;

import java.util.Map;

public class TextPost extends Post {

    private String title;
    private String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    protected Map<String, String> detail() {
        Map<String, String> map = super.detail();
        map.put("title", this.title);
        map.put("body", this.body);
        map.put("type", "text");
        return map;
    }

}
