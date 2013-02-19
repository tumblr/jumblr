import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.ChatPost;
import com.tumblr.jumblr.types.Dialogue;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.QuotePost;
import com.tumblr.jumblr.types.Video;
import com.tumblr.jumblr.types.VideoPost;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        // Read in the JSON data for the credentials
        FileReader fr = new FileReader("credentials.json");
        BufferedReader br = new BufferedReader(fr);
        String json = "";
        while (br.ready()) { json += br.readLine(); }

        // Parse the credentials
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(json);

        // Create a client
        JumblrClient client = new JumblrClient(
            obj.getAsJsonPrimitive("consumer_key").getAsString(),
            obj.getAsJsonPrimitive("consumer_secret").getAsString()
        );

        // Give it a token
        client.setToken(
            obj.getAsJsonPrimitive("oauth_token").getAsString(),
            obj.getAsJsonPrimitive("oauth_token_secret").getAsString()
        );

        // And make some calls - 2 posts for each blog
        for (Blog blog : client.user().getBlogs()) {
            System.out.println(blog.getTitle());

            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("limit", 2);
            for (Post post : blog.posts(map)) {
                System.out.println(post.toString());
            }

        }

        // @TODO play more with usage to ensure usability

        Map<String, String> options = new HashMap<String, String>();
        options.put("type", "video");
        options.put("limit", "1");
        Blog blog = client.blogInfo("david.tumblr.com");

        Post post = blog.posts(options).get(0);
        VideoPost cpost = (VideoPost) post;

        for (Video v : cpost.getVideos()) {
            System.out.println(v.getEmbedCode());
        }


    }

}
