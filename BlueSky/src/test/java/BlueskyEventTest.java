import org.junit.jupiter.api.Test;
import viper.sentinox.model.BlueskyEvent;

import static org.junit.jupiter.api.Assertions.*;

class BlueskyEventTest {

    @Test
    void constructor_storesValuesCorrectly() {

        BlueskyEvent event = new BlueskyEvent(
                1L,
                "BlueskyFeeder",
                "ibuprofen",
                "user.bsky.social",
                "Ibuprofen helped me",
                "Positive",
                "2026-05-01T10:00:00Z"
        );

        assertEquals(1L, event.ts());
        assertEquals("BlueskyFeeder", event.ss());
        assertEquals("ibuprofen", event.medicine());
        assertEquals("user.bsky.social", event.author());
        assertEquals("Ibuprofen helped me", event.text());
        assertEquals("Positive", event.sentiment());
        assertEquals("2026-05-01T10:00:00Z", event.createdAt());
    }
}