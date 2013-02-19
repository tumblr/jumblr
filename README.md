# Jumblr.

A pleasant and complete Java wrapper for the Tumblr V2 API.

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
