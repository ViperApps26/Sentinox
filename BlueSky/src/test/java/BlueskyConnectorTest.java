import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.BlueskyConnector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlueskyConnectorTest {

    private BlueskyConnector blueskyConnector;

    @BeforeEach
    void setUp() {
        blueskyConnector = new BlueskyConnector();
    }

    @Test
    void setQuery_updatesQuery() {
        blueskyConnector.setQuery("ibuprofeno");

        assertEquals("ibuprofeno", blueskyConnector.getQuery());
    }

    @Test
    void setLimit_updatesLimit() {
        blueskyConnector.setLimit(5);

        assertEquals(5, blueskyConnector.getLimit());
    }

    @Test
    void setStartDate_withValidDate_updatesStartDate() {
        blueskyConnector.setStartDate("2025-03-10");

        assertEquals("2025-03-10T00:00:00Z", blueskyConnector.getStartDate());
    }

    @Test
    void setFinalDate_withValidDate_updatesFinalDate() {
        blueskyConnector.setFinalDate("2025-04-15");

        assertEquals("2025-04-15T00:00:00Z", blueskyConnector.getFinalDate());
    }

    @Test
    void setStartDate_withInvalidDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                blueskyConnector.setStartDate("fecha-mal")
        );
    }

    @Test
    void setFinalDate_withInvalidDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                blueskyConnector.setFinalDate("15/04/2025")
        );
    }
}