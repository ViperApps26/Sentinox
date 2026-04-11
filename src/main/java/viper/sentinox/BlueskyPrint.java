package viper.sentinox;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

public class BlueskyPrint {
    public static void printPosts(String blueskyToken) throws IOException {
        JsonArray postAttributes = BlueskyGet.getPostsAttributes(blueskyToken);
        List<String> posts = BlueskyGet.getPosts(postAttributes);

        if (posts.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String post : posts) {
                System.out.println(post);
            }
        }
    }
}
