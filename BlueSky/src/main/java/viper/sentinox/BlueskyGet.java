package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyGet {

    private final BlueskyConnect blueskyConnect;

    public BlueskyGet(BlueskyConnect blueskyConnect) {
        this.blueskyConnect = blueskyConnect;
    }

    public JsonArray getPostsAttributes(String token) throws IOException {
        JsonObject object = blueskyConnect.connect(token);
        return object.getAsJsonArray("posts");
    }

    public List<String> getPosts(JsonArray postAttributes) {
        List<String> posts = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject post = postAttributes.get(i).getAsJsonObject();
            posts.add(
                    post.getAsJsonObject("record")
                            .get("text")
                            .getAsString()
            );
        }
        return posts;
    }

    public List<String> getAuthors(JsonArray postAttributes) {
        List<String> authors = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject post = postAttributes.get(i).getAsJsonObject();
            authors.add(
                    post.getAsJsonObject("author")
                            .get("handle")
                            .getAsString()
            );
        }
        return authors;
    }

    public List<String> getCreationDates(JsonArray postAttributes) {
        List<String> dates = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject post = postAttributes.get(i).getAsJsonObject();
            dates.add(
                    post.getAsJsonObject("record")
                            .get("createdAt")
                            .getAsString()
            );
        }
        return dates;
    }
}