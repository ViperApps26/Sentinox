import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.datamart.BusinessUnitEventHandler;
import viper.sentinox.control.datamart.MedicineDataMart;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BusinessUnitEventHandlerTest {

    private MedicineDataMart dataMart;
    private BusinessUnitEventHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        dataMart = new MedicineDataMart();
        handler = new BusinessUnitEventHandler(dataMart);
    }

    @Test
    void handleEvent_registersBlueskyEvent() {
        String json = """
                {
                  "medicine": "ibuprofen",
                  "author": "user.bsky.social",
                  "text": "Ibuprofen caused headache",
                  "sentiment": "Negative",
                  "createdAt": "2026-05-01T10:00:00Z"
                }
                """;

        handler.handleEvent(json, "BlueskyPosts");

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
    }

    @Test
    void handleEvent_registersPubChemEvent() {
        String json = """
                {
                  "medicine": "ibuprofen",
                  "cid": "3672",
                  "reaction": "Headache"
                }
                """;

        handler.handleEvent(json, "PubChemReactions");

        assertEquals(1, dataMart.getMedicineReactions("ibuprofen").size());
    }

    @Test
    void handleEvent_ignoresUnknownTopic() {
        String json = """
                {
                  "medicine": "ibuprofen",
                  "reaction": "Headache"
                }
                """;

        handler.handleEvent(json, "UnknownTopic");

        assertTrue(dataMart.getAllStats().isEmpty());
    }
}
