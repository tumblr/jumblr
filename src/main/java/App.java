import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        FileReader fr = new FileReader("credentials.json");
        BufferedReader br = new BufferedReader(fr);

        String json = "";
        while (br.ready()) { json += br.readLine(); }

        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(json);

        JumblrClient client = new JumblrClient(
            obj.getAsJsonPrimitive("consumer_key").getAsString(),
            obj.getAsJsonPrimitive("consumer_secret").getAsString()
        );

        client.setToken(
            obj.getAsJsonPrimitive("oauth_token").getAsString(),
            obj.getAsJsonPrimitive("oauth_token_secret").getAsString()
        );

        for (Blog blog : client.user().getBlogs()) {
            System.out.println(blog.getTitle());

            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("limit", 2);
            for (Post post : blog.posts(map)) {
                System.out.println(post.toString());
            }

            System.exit(0);

        }

        System.exit(0);

        /**
         * Other calls
         */

        Post rpost = client.userLikes().get(0);
        client.postReblog("apeyes.tumblr.com", rpost.getId(), rpost.getReblogKey());

        // Play with users
        User user = client.user();
        System.out.printf("%s is following %d blogs!\n", user.getName(), user.getFollowing());

        HashMap<String, Integer> options = new HashMap<String, Integer>();
        options.put("limit", 2);
        for (Post post : client.userDashboard(options)) {
            System.out.println(post.getReblogKey());
        }

        Blog my = null;
        for (Blog blog : user.getBlogs()) {
            System.out.println(blog.getName());
            my = blog;
        }

        for (Post post : client.blogSubmissions(my.getName())) {
            System.out.println(post.getId());
        }

        for (Post post : client.blogQueuedPosts(my.getName())) {
            System.out.println(post.getId());
        }

        for (Post post : my.draftPosts()) {
            System.out.println(post.getId());
        }

        for (Post post : my.submissions()) {
            System.out.println(post.getId());
        }

        // Get the blogs I'm following
        Blog lb = null;
        for (Blog blog : client.userFollowing()) {
            System.out.println(blog.getName());
            lb = blog;
        }

        client.follow(lb.getName());
        lb.follow();


        // Play with a blog
        Blog blog = client.blogInfo("seejohnrun.tumblr.com");
        System.out.println(blog.getTitle());
        System.out.println(blog.avatar());

        // Get our followers
        for (User follower : blog.followers()) {
            System.out.println(follower.getName());
        }

        // Get our likes
        Post last = null;
        for (Post post : blog.likedPosts()) {
            System.out.println(post.getId());
            last = post;
        }

        for (Post post : client.userLikes()) {
            System.out.println(post.getReblogKey());
        }

        // Like that last post
        client.like(last.getId(), last.getReblogKey());
        last.like();

    }

}
