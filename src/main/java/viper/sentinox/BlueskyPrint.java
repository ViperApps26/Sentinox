package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyPrint {
    public static void printPosts(String blueskyToken, int quantity) throws IOException {
        JsonObject object = BlueskyConnect.connect(blueskyToken);

        JsonArray postAttributes = object.getAsJsonArray("posts");

        if (postAttributes.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            List<String> posts = getPosts(postAttributes, quantity);
            for (String post : posts) {
                System.out.println(post);
            }
        }
    }

    public static List<String> getPosts(JsonArray posts, int quantity) {
        List<String> postsArray = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            JsonObject post = posts.get(i).getAsJsonObject();
            postsArray.add(formatPost(post));
        }
        return postsArray;
    }

    public static String formatPost(JsonObject post) {
        return post
                .getAsJsonObject("record")
                .get("text")
                .getAsString();
    }
}
