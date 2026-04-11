import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.BlueskyConnect;
import viper.sentinox.BlueskyPrint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlueskyPrintTest {

    private BlueskyPrint blueskyPrint;

    @BeforeEach
    void setUp() {
        blueskyPrint = new BlueskyPrint(new BlueskyConnect());
    }

    @Test
    void formatPost_returnsPostText() {
        JsonObject post = createPost("Hola mundo");

        String result = blueskyPrint.formatPost(post);

        assertEquals("Hola mundo", result);
    }

    @Test
    void getPosts_returnsOnePostWhenQuantityIsOne() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));
        posts.add(createPost("Post 2"));

        List<String> result = blueskyPrint.getPosts(posts, 1);

        assertEquals(1, result.size());
    }

    @Test
    void getPosts_returnsFirstPostText() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));

        List<String> result = blueskyPrint.getPosts(posts, 1);

        assertEquals("Post 1", result.get(0));
    }

    @Test
    void getPosts_returnsSecondPostText() {
        JsonArray posts = new JsonArray();
        posts.add(createPost("Post 1"));
        posts.add(createPost("Post 2"));

        List<String> result = blueskyPrint.getPosts(posts, 2);

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