import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.BlueskyConnect;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlueskyConnectTest {

    @AfterEach
    void resetValues() {
        BlueskyConnect.query = "nolotil";
        BlueskyConnect.limit = 10;
        BlueskyConnect.startDate = "2025-01-01T00:00:00Z";
        BlueskyConnect.finalDate = "2025-02-01T00:00:00Z";
    }

    @Test
    void setQuery_updatesQuery() {
        BlueskyConnect.setQuery("ibuprofeno");
        assertEquals("ibuprofeno", BlueskyConnect.query);
    }

    @Test
    void setLimit_updatesLimit() {
        BlueskyConnect.setLimit(5);
        assertEquals(5, BlueskyConnect.limit);
    }

    @Test
    void setStartDate_withValidDate_updatesStartDate() {
        BlueskyConnect.setStartDate("2025-03-10");

        assertEquals("2025-03-10T00:00:00Z", BlueskyConnect.startDate);
    }

    @Test
    void setFinalDate_withValidDate_updatesFinalDate() {
        BlueskyConnect.setFinalDate("2025-04-15");

        assertEquals("2025-04-15T00:00:00Z", BlueskyConnect.finalDate);
    }

    @Test
    void setStartDate_withInvalidDate_keepsPreviousValue() {
        BlueskyConnect.setStartDate("fecha-mal");

        assertEquals("2025-01-01T00:00:00Z", BlueskyConnect.startDate);
    }

    @Test
    void setFinalDate_withInvalidDate_keepsPreviousValue() {
        BlueskyConnect.setFinalDate("15/04/2025");

        assertEquals("2025-02-01T00:00:00Z", BlueskyConnect.finalDate);
    }
}

