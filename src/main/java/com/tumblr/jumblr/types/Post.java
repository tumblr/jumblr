package com.tumblr.jumblr.types;

import com.tumblr.jumblr.JumblrClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Post {

    private Long id;
    private String reblog_key;
    private String blog_name;
    private String post_url;
    private String type;
    private Long timestamp;
    private String state;
    private String format;
    private String date;
    private List<String> tags;
    private Boolean bookmarklet, mobile;
    private String source_url, source_title;
    private Boolean liked;
    private String slug;

    protected JumblrClient client;

    public Boolean isLiked() {
        return liked;
    }

    public String getSourceTitle() {
        return source_title;
    }

    public String getSourceUrl() {
        return source_url;
    }

    public Boolean isMobile() {
        return mobile;
    }

    public Boolean isBookmarklet() {
        return bookmarklet;
    }

    public String getFormat() {
        return format;
    }

    public String getState() {
        return state;
    }

    public String getPostUrl() {
        return post_url;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDateGMT() {
        return date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public void setClient(JumblrClient client) {
        this.client = client;
    }

    public String getReblogKey() {
        return this.reblog_key;
    }

    /**
     * Delete this post
     */
    public void delete() {
        client.postDelete(blog_name, id);
    }

    /**
     * Reblog this post
     * @param blogName the blog name to reblog onto
     * @param options options to reblog with (or null)
     * @return reblogged post
     */
    public Post reblog(String blogName, Map<String, ?> options) {
        return client.postReblog(blogName, id, reblog_key, options);
    }

    public Post reblog(String blogName) {
        return this.reblog(blogName, null);
    }

    /**
     * Like this post
     */
    public void like() {
        client.like(this.id, this.reblog_key);
    }

    /**
     * Unlike this post
     */
    public void unlike() {
        client.unlike(this.id, this.reblog_key);
    }

    /**
     * Set the blog name for this post
     * @param blogName the blog name to set
     */
    public void setBlogName(String blogName) {
        blog_name = blogName;
    }

    /**
     * Set the id for this post
     * @param id The id of the post
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Set the format
     * @param format the format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Set the slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Set the state for this post
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Set the tags for this post
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Add a tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            tags = new ArrayList<String>();
        }
        this.tags.add(tag);
    }

    /**
     * Remove a tag
     */
    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    /**
     * Save this post
     */
    public void save() {
        if (id == null) {
            this.id = client.postCreate(blog_name, detail());
        } else {
            client.postEdit(blog_name, id, detail());
        }
    }

    /**
     * Detail for this post
     */
    protected Map<String, String> detail() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("state", state);
        map.put("tags", getTagString());
        map.put("format", format);
        map.put("slug", slug);
        return map;
    }

    private String getTagString() {
        return tags == null ? "" : StringUtils.join((String[]) tags.toArray(), ",");
    }

    /**
     * Post toString
     * @return a nice representation of this post
     */
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + " (" + blog_name + ":" + id + ")]";
    }

}
