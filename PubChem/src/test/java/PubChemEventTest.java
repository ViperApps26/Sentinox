import org.junit.jupiter.api.Test;
import viper.sentinox.model.PubChemEvent;

import static org.junit.jupiter.api.Assertions.*;

class PubChemEventTest {

    @Test
    void constructor_storesValuesCorrectly() {
        PubChemEvent event = new PubChemEvent(
                1L,
                "PubChemFeeder",
                "ibuprofen",
                "3672",
                "Headache"
        );

        assertEquals(1L, event.ts());
        assertEquals("PubChemFeeder", event.ss());
        assertEquals("ibuprofen", event.medicine());
        assertEquals("3672", event.cid());
        assertEquals("Headache", event.reaction());
    }
}