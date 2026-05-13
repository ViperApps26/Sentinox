import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.EventStoreBuilder;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class EventStoreBuilderTest {

    private Path eventstorePath;

    @BeforeEach
    void setUp() throws Exception {
        eventstorePath = Path.of("eventstore");
        deleteEventStoreIfExists();
    }

    @AfterEach
    void tearDown() throws Exception {
        deleteEventStoreIfExists();
    }

    @Test
    void handleEvent_writesEventIntoCorrectFile() throws Exception {
        EventStoreBuilder builder = new EventStoreBuilder(
                "tcp://localhost:61616",
                "TestEventStore"
        );

        String json = """
                {
                  "ts": 1777639200000,
                  "ss": "BlueskyFeeder",
                  "medicine": "ibuprofen",
                  "author": "user.bsky.social",
                  "text": "Ibuprofen helped me",
                  "sentiment": "Positive",
                  "createdAt": "2026-05-01T10:00:00Z"
                }
                """;

        Method method = EventStoreBuilder.class.getDeclaredMethod(
                "handleEvent",
                String.class,
                String.class
        );

        method.setAccessible(true);
        method.invoke(builder, json, "BlueskyPosts");

        Path expectedFile = eventstorePath
                .resolve("BlueskyPosts")
                .resolve("BlueskyFeeder")
                .resolve("20260501.events");

        assertTrue(Files.exists(expectedFile));

        String content = Files.readString(expectedFile);

        assertTrue(content.contains("\"medicine\": \"ibuprofen\"")
                || content.contains("\"medicine\":\"ibuprofen\""));
    }

    private void deleteEventStoreIfExists() throws Exception {
        if (!Files.exists(eventstorePath)) {
            return;
        }

        Files.walk(eventstorePath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception ignored) {
                    }
                });
    }
}