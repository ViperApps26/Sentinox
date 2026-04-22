package viper.sentinox.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyGet implements BlueskyGetInterface {

    private final BlueskyConnector connector;

    public BlueskyGet(BlueskyConnector connector) {
        this.connector = connector;
    }

    public JsonArray getPostsAttributes(String token) throws IOException {
        JsonObject object = connector.connector(token);

        if (object == null || !object.has("posts")) {
            return new JsonArray();
        }

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