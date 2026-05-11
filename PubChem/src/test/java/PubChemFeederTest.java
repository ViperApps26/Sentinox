import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.model.PubChemEvent;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PubChemFeederTest {

    private PubChemFeeder pubChemFeeder;

    @BeforeEach
    void setUp() {
        pubChemFeeder = new PubChemFeeder();
    }

    @Test
    void get_returnsEventsForMedicine() throws IOException {
        List<PubChemEvent> events = pubChemFeeder.get("ibuprofen");

        assertNotNull(events);
        assertFalse(events.isEmpty());

        PubChemEvent firstEvent = events.get(0);

        assertEquals("PubChemFeeder", firstEvent.ss());
        assertEquals("ibuprofen", firstEvent.medicine());
        assertNotNull(firstEvent.cid());
        assertFalse(firstEvent.cid().isBlank());
        assertNotNull(firstEvent.reaction());
        assertFalse(firstEvent.reaction().isBlank());
    }
}