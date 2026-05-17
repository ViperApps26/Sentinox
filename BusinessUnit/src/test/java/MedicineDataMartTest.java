import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.datamart.MedicineDataMart;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MedicineDataMartTest {

    private MedicineDataMart dataMart;

    @BeforeEach
    void setUp() throws IOException {
        dataMart = new MedicineDataMart();
    }

    @Test
    void registerBlueskyEvent_storesComment() {
        dataMart.registerBlueskyEvent(
                "ibuprofen",
                "user.bsky.social",
                "Ibuprofen caused headache",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
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
    }

    @Test
    void registerPubChemEvent_storesReaction() {
        dataMart.registerPubChemEvent("ibuprofen", "Headache");

        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
        assertEquals("Headache", dataMart.getMedicineReactions("ibuprofen").getFirst());
    }

    @Test
    void registerPubChemEvent_doesNotDuplicateReaction() {
        dataMart.registerPubChemEvent("ibuprofen", "Headache");
        dataMart.registerPubChemEvent("ibuprofen", "Headache");

        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
    }

    @Test
    void registerPubChemEvent_ignoresGenericReaction() {
        dataMart.registerPubChemEvent("ibuprofen", "Drug Interactions");

        assertTrue(dataMart.getMedicineReactions("ibuprofen").isEmpty());
    }

    @Test
    void getAllMedicinesSorted_returnsAlphabeticalList() {
        dataMart.registerPubChemEvent("naproxen", "Headache");
        dataMart.registerPubChemEvent("aspirin", "Nausea");

        assertEquals("aspirin", dataMart.getAllMedicinesSorted().get(0));
        assertEquals("naproxen", dataMart.getAllMedicinesSorted().get(1));
    }

    @Test
    void registerEvents_updatesJointAnalysis() {
        dataMart.registerPubChemEvent("ibuprofen", "headache");

        dataMart.registerBlueskyEvent(
                "ibuprofen",
                "user1",
                "Ibuprofen caused headache",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        assertEquals(
                1,
                dataMart.getMedicineJointAnalysis("ibuprofen").getMatchedReactions()
        );
    }
}
