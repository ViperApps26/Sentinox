package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyGet {

    private final BlueskyConnect blueskyConnect;

    public BlueskyGet (BlueskyConnect blueskyConnect) {
        this.blueskyConnect = blueskyConnect;
    }

    public JsonArray getPostsAttributes(String blueskyToken) throws IOException {
        JsonObject object = blueskyConnect.connect(blueskyToken);
        return object.getAsJsonArray("posts");
    }

    public List<String> getPosts(JsonArray postAttributes) {
        List<String> posts = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject post = postAttributes.get(i).getAsJsonObject();
            posts.add(formatPost(post));
        }
        return posts;
    }

    public String formatPost(JsonObject post) {
        return post
                .getAsJsonObject("record")
                .get("text")
                .getAsString();
    }

    public List<String> getAuthors(JsonArray postAttributes) {

        List<String> authors = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject author = postAttributes.get(i).getAsJsonObject();
            authors.add(formatAuthor(author));
        }
        return authors;
    }

    public String formatAuthor(JsonObject author) {
        return author
                .getAsJsonObject("author")
                .get("handle")
                .getAsString();
    }

    public List<String> getCreationDate(JsonArray postAttributes) {

        List<String> dates = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject date = postAttributes.get(i).getAsJsonObject();
            dates.add(formatCreatedDate(date));
        }
        return dates;
    }

    public String formatCreatedDate(JsonObject date) {
        return date
                .getAsJsonObject("record")
                .get("createdAt")
                .getAsString();
    }
}
