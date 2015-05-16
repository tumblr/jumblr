package com.tumblr.jumblr.types;

/**
 * If we can't deserialize a Post to a proper type, we'll return one of
 * these bad boys so that people can still access the base Post attributes
 * @author john
 */
public class UnknownTypePost extends SafePost {

}
