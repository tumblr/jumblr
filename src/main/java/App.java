import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;
import com.tumblr.jumblr.types.User;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example usage of Jumblr
 * @author jc
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {

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

        // Write a text post but don't include the necessary details

        Blog blog = client.blogInfo("seejohnrun.tumblr.com");
        TextPost post = blog.newPost(TextPost.class);

        try {
            post.save();
        } catch (JumblrException ex) {
            System.out.println(ex.getMessage());
            List<String> errors = ex.getErrors();
            if (errors != null) {
                for (String error : ex.getErrors()) {
                    System.out.println("\t" + error);
                }
            }
        }

    }

}
