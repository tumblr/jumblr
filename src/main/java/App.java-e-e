import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;

public class App {

    public static void main(String[] args) {
        
        JumblrClient client = new JumblrClient(
            "consumer_key",
            "consumer_secret"
        );
        
        client.setToken(
            "oauth_token",
            "oauth_token_secret"
        );
        
        // Play with users
        User user = client.userInfo();
        System.out.printf("%s is following %d blogs!\n", user.getName(), user.getFollowing());
        for (Blog blog : user.getBlogs()) {
            System.out.println(blog.getName());
        }
        
        // Get the blogs I'm following
        for (Blog blog : client.userFollowing()) {
            System.out.println(blog.getName());
        }
        
        // Play with a blog
        Blog blog = client.blogInfo("seejohnrun.tumblr.com");
        System.out.println(blog.getTitle());
        System.out.println(blog.getAvatar());
        
        // Get our followers
        for (User follower : blog.getFollowers()) {
            System.out.println(follower.getName());
        }
        
        // Get our likes
        for (Post post : blog.getLikedPosts()) {
            System.out.println(post.getId());
        }
        
    }
    
}
