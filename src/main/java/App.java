import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.User;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
        br.close();

        // Parse the credentials
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(json);

        // Create a client
        JumblrClient client = new JumblrClient(
            obj.getAsJsonPrimitive("consumer_key").getAsString(),
            obj.getAsJsonPrimitive("consumer_secret").getAsString()
        );

        boolean b = client.authenticate();
        if (!b) {
            System.out.println("Failed to authenticate. This could be due to network errors, or " +
            		"the user denying the access request.");
        }
        
        // Usage
        User user = client.user();
        System.out.printf("User %s has these blogs:%n", user.getName());

        // And list their blogs
        for (Blog blog : user.getBlogs()) {
            System.out.printf("\t%s (%s)%n", blog.getName(), blog.getTitle());
        }
        
        System.out.println("They are following these blogs:");
        List<Blog> blogs = client.userFollowing();
        for (Blog blog : blogs) {
            System.out.printf("\t%s (%s)%n", blog.getName(), blog.getTitle());
        }
    }

}
