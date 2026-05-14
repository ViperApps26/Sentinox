import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.datamart.MedicineDataMart;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MedicineDataMartTest {

    private MedicineDataMart dataMart;

    @BeforeEach
    void setUp() {
        dataMart = new MedicineDataMart();
    }

    @Test
    void registerBlueskyEvent_storesCommentAndSentiment() {
        dataMart.registerBlueskyEvent(
                "ibuprofen",
                "user.bsky.social",
                "Ibuprofen helped me",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
        assertEquals(1, dataMart.getMedicineSentimentPositive("ibuprofen"));
        assertEquals(0, dataMart.getMedicineSentimentNegative("ibuprofen"));
        assertEquals(0, dataMart.getMedicineSentimentNeutral("ibuprofen"));
    }

    @Test
    void registerBlueskyEvent_doesNotDuplicateSameCommentText() {
        dataMart.registerBlueskyEvent(
                "ibuprofen",
                "user1",
                "Same comment",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        dataMart.registerBlueskyEvent(
                "ibuprofen",
                "user2",
                "Same comment",
                "Negative",
                Instant.parse("2026-05-02T10:00:00Z")
        );

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
        assertEquals(1, dataMart.getMedicineSentimentPositive("ibuprofen"));
        assertEquals(0, dataMart.getMedicineSentimentNegative("ibuprofen"));
    }

    @Test
    void registerPubChemEvent_storesReaction() {
        dataMart.registerPubChemEvent("ibuprofen", "Headache");

        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
        assertEquals("Headache", dataMart.getMedicineReactions("ibuprofen").get(0));
    }

    @Test
    void registerPubChemEvent_doesNotDuplicateReaction() {
        dataMart.registerPubChemEvent("ibuprofen", "Headache");
        dataMart.registerPubChemEvent("ibuprofen", "Headache");

        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
    }

    @Test
    void getAllStats_returnsStoredMedicines() {
        dataMart.registerPubChemEvent("ibuprofen", "Headache");

        assertTrue(dataMart.getAllStats().containsKey("ibuprofen"));
    }
}