package viper.sentinox.control;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

public interface BlueskyGetInterface {
    JsonArray getPostsAttributes(String token) throws IOException;

    List<String> getPosts(JsonArray postAttributes);
    List<String> getAuthors(JsonArray postAttributes);
    List<String> getCreationDates(JsonArray postAttributes);
}
