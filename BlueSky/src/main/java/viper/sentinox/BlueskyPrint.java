package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyPrint {

    private final BlueskyConnect blueskyConnect;

    public BlueskyPrint(BlueskyConnect blueskyConnect) {
        this.blueskyConnect = blueskyConnect;
    }

    public void printPosts(String blueskyToken, int quantity) throws IOException {
        JsonObject object = blueskyConnect.connect(blueskyToken);
        JsonArray postAttributes = object.getAsJsonArray("posts");

        if (postAttributes == null || postAttributes.isEmpty()) {
            System.out.println("No Results Found");
            return;
        }

        List<String> posts = getPosts(postAttributes, quantity);
        for (String post : posts) {
            System.out.println(post);
        }
    }

    public List<String> getPosts(JsonArray posts, int quantity) {
        List<String> postsArray = new ArrayList<>();
        int maxPosts = Math.min(quantity, posts.size());

        for (int i = 0; i < maxPosts; i++) {
            JsonObject post = posts.get(i).getAsJsonObject();
            postsArray.add(formatPost(post));
        }

        return postsArray;
    }

    public String formatPost(JsonObject post) {
        return post
                .getAsJsonObject("record")
                .get("text")
                .getAsString();
    }
}