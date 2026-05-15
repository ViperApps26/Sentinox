import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.control.DataMartFeader.BusinessUnitEventHandler;
import viper.sentinox.control.DataMartFeader.EventStoreReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class EventStoreReaderTest {

    private Path eventstorePath;

    @BeforeEach
    void setUp() throws IOException {
        eventstorePath = Path.of("eventstore");
        deleteEventStoreIfExists();
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteEventStoreIfExists();
    }

    @Test
    void loadHistoricalEvents_loadsBlueskyAndPubChemEvents() throws IOException {
        Path blueskyDir = eventstorePath.resolve("BlueskyPosts").resolve("BlueskyFeeder");
        Path pubchemDir = eventstorePath.resolve("PubChemReactions").resolve("PubChemFeeder");

        Files.createDirectories(blueskyDir);
        Files.createDirectories(pubchemDir);

        Files.writeString(
                blueskyDir.resolve("20260501.events"),
                """
                {"medicine":"ibuprofen","author":"user.bsky.social","text":"Ibuprofen helped me","sentiment":"Positive","createdAt":"2026-05-01T10:00:00Z"}
                """
        );

        Files.writeString(
                pubchemDir.resolve("20260501.events"),
                """
                {"medicine":"ibuprofen","cid":"3672","reaction":"Headache"}
                """
        );

        MedicineDataMart dataMart = new MedicineDataMart();
        BusinessUnitEventHandler handler = new BusinessUnitEventHandler(dataMart);
        EventStoreReader reader = new EventStoreReader(handler);

        reader.loadHistoricalEvents();

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
        assertEquals(1, dataMart.getMedicineSentimentPositive("ibuprofen"));
    }

    private void deleteEventStoreIfExists() throws IOException {
        if (!Files.exists(eventstorePath)) {
            return;
        }

        Files.walk(eventstorePath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
    }
}