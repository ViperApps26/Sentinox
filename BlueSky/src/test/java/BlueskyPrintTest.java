import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.model.BlueskyConnect;
import viper.sentinox.model.BlueskyGet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlueskyPrintTest {

    private BlueskyGet blueskyGet;

    @BeforeEach
    void setUp() {
        blueskyGet = new BlueskyGet(new BlueskyConnect());
    }

    @Test
    void getPosts_returnsOnePostWhenQuantityIsOne() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));
        posts.add(createPost("Post 2"));

        List<String> result = blueskyGet.getPosts(posts);

        assertEquals(1, result.size());
    }

    @Test
    void getPosts_returnsFirstPostText() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));

        List<String> result = blueskyGet.getPosts(posts);

        assertEquals("Post 1", result.getFirst());
    }

    @Test
    void getPosts_returnsSecondPostText() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));
        posts.add(createPost("Post 2"));

        List<String> result = blueskyGet.getPosts(posts);

        assertEquals("Post 2", result.get(1));
    }

    private JsonObject createPost(String text) {
        JsonObject record = new JsonObject();
        record.addProperty("text", text);

        JsonObject post = new JsonObject();
        post.add("record", record);

        return post;
    }
}