package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyGet {
    public static JsonArray getPostsAttributes(String blueskyToken) throws IOException {
        JsonObject object = BlueskyConnect.connect(blueskyToken);
        return object.getAsJsonArray("posts");
    }

    public static List<String> getPosts(JsonArray postAttributes) {
        List<String> posts = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject post = postAttributes.get(i).getAsJsonObject();
            posts.add(formatPost(post));
        }
        return posts;
    }

    public static String formatPost(JsonObject post) {
        return post
                .getAsJsonObject("record")
                .get("text")
                .getAsString();
    }

    public static List<String> getAuthors(JsonArray postAttributes) {

        List<String> authors = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject author = postAttributes.get(i).getAsJsonObject();
            authors.add(formatAuthor(author));
        }
        return authors;
    }

    public static String formatAuthor(JsonObject author) {
        return author
                .getAsJsonObject("author")
                .get("handle")
                .getAsString();
    }

    public static List<String> getUris(JsonArray postAttributes) {

        List<String> uris = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject uri = postAttributes.get(i).getAsJsonObject();
            uris.add(formatUri(uri));
        }
        return uris;
    }

    public static String formatUri(JsonObject uri) {
        return uri
                .get("uri")
                .getAsString();
    }

    public static List<String> getCreationDate(JsonArray postAttributes) {

        List<String> dates = new ArrayList<>();

        for (int i = 0; i < postAttributes.size(); i++) {
            JsonObject date = postAttributes.get(i).getAsJsonObject();
            dates.add(formatCreatedDate(date));
        }
        return dates;
    }

    public static String formatCreatedDate(JsonObject date) {
        return date
                .getAsJsonObject("record")
                .get("createdAt")
                .getAsString();
    }
}
