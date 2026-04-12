package viper.sentinox;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

public class BlueskyPrint {

    private final BlueskyGet blueskyGet;

    public BlueskyPrint (BlueskyGet blueskyGet) {
        this.blueskyGet = blueskyGet;
    }

    public void printPosts(String blueskyToken) throws IOException {
        JsonArray postAttributes = blueskyGet.getPostsAttributes(blueskyToken);
        List<String> posts = blueskyGet.getPosts(postAttributes);

        if (posts.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String post : posts) {
                System.out.println(post);
            }
        }
    }
}