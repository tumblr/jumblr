import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;

public class App {

    public static void main(String[] args) {
        
        JumblrClient client = new JumblrClient(
            "xzGO5Gq1iF5aYb0pMGi9GHoqzICmWBkFg65FCsTrKtsG8aSFX7",
            "CK87YGNFc1EDmhrPbEMFVgOagwfvU6PMf6n6jslGH5nL1CVqyz"
        );
        
        client.setToken(
            "cGJ4bcdjJlABiWOC5aL6IEuNK8pKPVCFlgbCX7S8sL9noOTi7f",
            "QOIJidtdW1BdGrtISyOXM9GybESStetOb8eCHvZjBXhW9a70BB"
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