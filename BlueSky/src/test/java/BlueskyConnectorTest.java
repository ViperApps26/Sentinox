import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.feeder.BlueskyConnector;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BlueskyConnectorTest {

    private BlueskyConnector blueskyConnector;

    @BeforeEach
    void setUp() {
        blueskyConnector = new BlueskyConnector();
    }

    @Test
    void connector_returnsValidJsonObject() throws IOException {
        JsonObject result = blueskyConnector.connector("YOUR_ACCESS_TOKEN");

        assertNotNull(result);
        assertTrue(result.has("posts"));
    }

    @Test
    void setQuery_changesMedicineUsedInRequest() {
        blueskyConnector.setQuery("aspirin");

        assertEquals("aspirin", blueskyConnector.getQuery());
    }

    @Test
    void setLimit_changesLimitCorrectly() {
        blueskyConnector.setLimit(20);

        assertEquals(20, blueskyConnector.getLimit());
    }

    @Test
    void setStartDate_changesStartDateCorrectly() {
        blueskyConnector.setStartDate("2026-05-01");

        assertEquals(
                "2026-05-01T00:00:00Z",
                blueskyConnector.getStartDate()
        );
    }

    @Test
    void setFinalDate_changesFinalDateCorrectly() {
        blueskyConnector.setFinalDate("2026-05-10");

        assertEquals(
                "2026-05-10T00:00:00Z",
                blueskyConnector.getFinalDate()
        );
    }
}