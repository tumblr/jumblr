# Jumblr.

[![Build Status](https://secure.travis-ci.org/tumblr/jumblr.png?branch=master)](http://travis-ci.org/tumblr/jumblr)

A pleasant and complete Java wrapper for the Tumblr V2 API.

## Example

``` java
// Create a new client
JumblrClient client = new JumblrClient("consumer_key", "consumer_secret");
client.setToken("oauth_token", "oauth_token_secret");

// Write the user's name
User user = client.user();
System.out.println(user.getName());

// And list their blogs
for (Blog blog : user.getBlogs()) {
	System.out.println("\t" + blog.getTitle());
}

// Like the most recent "lol" tag
client.tagged("lol").get(0).like();
```

## Basic Usage

### User

Information about the authenticating user

``` java
User user = client.user();
System.out.println(user.getName());
```

What blogs the user is following

``` java
List<Blog> blogs = client.userFollowing();
for (Blog blog : blogs) {
	System.out.println(blog.getTitle());
}
```

Dashboard posts for the user?

``` java
List<Post> posts = client.userDashboard();
```

Or the things we like:

``` java
List<Post> posts = client.userLikes();
```

### Blog

Get information about a given blog

``` java
Blog blog = client.blogInfo("seejohnrun.tumblr.com");
blog.getTitle();
```

And then you can use either the `client` approach, or make calls on the blog.
These two are equivelant:

``` java
List<User> users = client.blogFollowers();
List<User> users = blog.followers();
```

Same with likes, etc:

``` java
// All return List<Post>
blog.posts();
blog.queuedPosts();
blog.draftPosts();
blog.submissions();
```

It's easy to get get the avatar of a blog too:

``` java
blog.avatar(); // String
```

And follow/unfollow blogs:

``` java
blog.follow();
blog.unfollow();
```

### Post

Again:

``` java
post.like();
post.unlike();

// or
client.postLike(postId, reblogKey);
client.postUnlike(postId, reblogKey);
```

Maybe you want to delete a post, or reblog it?

``` java
// Delete
post.delete();

// Reblog
post.reblog();
```

And editing posts is equally easy:

``` java
post.setTitle("hello");
post.save();

// or if you like..
Map<String, String> detail = new HashMap<String, String>();
detail.put("title", "hello");
client.postEdit(blogName, postId, detail);
```

Just as easy as creating them:

``` java
QuotePost post = client.newPost(blogName, QuotePost.class);
post.setQuote("hello world");
post.save();

// or if you like...
Map<String, String> detail = new HashMap<String, String>();
detail.put("quote", "hello world");
detail.put("type", "quote");
client.postCreate(blogName, detail);
```

### Tagged

All of the posts tagged a given thing:

``` java
for (Post post : client.tagged("lol")) {
  post.getId();
}
```

### Pagination

Pagination and additional options are on most calls, and you can just pass them
to the overloaded form:

``` java
Map<String, String> options = new HashMap<String, String>();
options.put("limit", 2);
options.put("offset", 5);
List<Post> posts = blog.posts(options);
```

### Errors

Any connection errors will raise a `JumblrException`, which you can get more
information from:

``` java
catch (JumblrException ex) {
	System.out.println("(" + ex.getResponseCode().toString() + ") " + ex.getMessage());
}
```

## Maven

``` xml
<dependency>
  <groupId>com.tumblr</groupId>
  <artifactId>jumblr</artifactId>
  <version>0.0.6</version>
</dependency>
```

## Runnings Tests

`mvn test`

## More detail?

Pop out that JavaDoc - full documentation awaits.

Or you can use our hosted copy at:
http://tumblr.github.io/jumblr/javadoc/

Also see the documentation for the
[Tumblr API](http://www.tumblr.com/docs/en/api/v2) itself.

## Builds

We supply some JAR builds you can use if you're not a fan of mvn.
You can grab them with (or without) dependencies from
[the project page](http://tumblr.github.io/jumblr).

## Reporting issues

Please refer to the GitHub issue tracker at:
https://github.com/tumblr/jumblr/issues

## Copyright and license

Copyright 2013 Tumblr, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this work except in compliance with the License. You may obtain a copy of
the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations.
