# Jumblr.

A pleasant Java wrapper for the Tumblr V2 API.

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
