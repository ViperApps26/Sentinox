import org.junit.jupiter.api.Test;
import viper.sentinox.model.Comment;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void constructor_storesValuesCorrectly() {
        Comment comment = new Comment(
                "user.bsky.social",
                "Ibuprofen gave me headache",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        assertEquals("user.bsky.social", comment.getAuthor());
        assertEquals("Ibuprofen gave me headache", comment.getText());
        assertEquals("Negative", comment.getSentiment());
        assertNotNull(comment.getDate());
    }
}