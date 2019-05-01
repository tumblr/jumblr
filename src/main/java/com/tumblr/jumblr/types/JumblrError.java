package com.tumblr.jumblr.types;

/**
 * This class represents an error that comes back from an API response.
 *
 * Example response with an error:
 * <code>
 * {
 *     "meta": {
 *         "status": 404,
 *         "msg": "Not Found"
 *     },
 *     "response": [],
 *     "errors": [
 *         {
 *             "title": "Not Found",
 *             "code": 4012,
 *             "detail": "This Tumblr is only viewable within the Tumblr dashboard"
 *         }
 *     ]
 * }
 * </code>
 * @author ndtreviv
 */
public class JumblrError {

    private String title;
    private Integer code;
    private String detail;

    /**
     * Get the error title
     * @return  error title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the error code
     * @return  error code
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Get the error detail
     * @return  error detail
     */
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
