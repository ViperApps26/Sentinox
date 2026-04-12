import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.BlueskyConnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlueskyConnectTest {

    private BlueskyConnect blueskyConnect;

    @BeforeEach
    void setUp() {
        blueskyConnect = new BlueskyConnect();
    }

    @Test
    void setQuery_updatesQuery() {
        blueskyConnect.setQuery("ibuprofeno");

        assertEquals("ibuprofeno", blueskyConnect.getQuery());
    }

    @Test
    void setLimit_updatesLimit() {
        blueskyConnect.setLimit(5);

        assertEquals(5, blueskyConnect.getLimit());
    }

    @Test
    void setStartDate_withValidDate_updatesStartDate() {
        blueskyConnect.setStartDate("2025-03-10");

        assertEquals("2025-03-10T00:00:00Z", blueskyConnect.getStartDate());
    }

    @Test
    void setFinalDate_withValidDate_updatesFinalDate() {
        blueskyConnect.setFinalDate("2025-04-15");

        assertEquals("2025-04-15T00:00:00Z", blueskyConnect.getFinalDate());
    }

    @Test
    void setStartDate_withInvalidDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                blueskyConnect.setStartDate("fecha-mal")
        );
    }

    @Test
    void setFinalDate_withInvalidDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                blueskyConnect.setFinalDate("15/04/2025")
        );
    }
}///test