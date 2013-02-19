import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;
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
        options.put("type", "photo");
        options.put("limit", "1");
        Blog blog = client.blogInfo("david.tumblr.com");

        Post post = blog.posts(options).get(0);
        PhotoPost cpost = (PhotoPost) post;

        Photo firstPhoto = cpost.getPhotos()[0];
        System.out.println(cpost.isPhotoset() ? firstPhoto.getCaption() : cpost.getCaption());
        System.out.println(firstPhoto.getSizes()[0].getUrl());

    }

}
