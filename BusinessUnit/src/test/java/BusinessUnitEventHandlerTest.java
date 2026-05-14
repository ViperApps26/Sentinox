import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.control.DataMartFeader.BusinessUnitEventHandler;

import static org.junit.jupiter.api.Assertions.*;

class BusinessUnitEventHandlerTest {

    private MedicineDataMart dataMart;
    private BusinessUnitEventHandler handler;

    @BeforeEach
    void setUp() {
        dataMart = new MedicineDataMart();
        handler = new BusinessUnitEventHandler(dataMart);
    }

    @Test
    void handleEvent_registersBlueskyEvent() {
        String json = """
                {
                  "medicine": "ibuprofen",
                  "author": "user.bsky.social",
                  "text": "Ibuprofen helped me",
                  "sentiment": "Positive",
                  "createdAt": "2026-05-01T10:00:00Z"
                }
                """;

        handler.handleEvent(json, "BlueskyPosts");

        assertEquals(1, dataMart.getMedicineComments("ibuprofen").size());
        assertEquals(1, dataMart.getMedicineSentimentPositive("ibuprofen"));
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
        assertEquals("Headache", dataMart.getMedicineReactions("ibuprofen").get(0));
    }
}