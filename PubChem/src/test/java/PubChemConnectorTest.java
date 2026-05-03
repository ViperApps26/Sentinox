import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import viper.sentinox.control.PubChemConnector;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PubChemConnectorTest {

    private PubChemConnector pubChemConnector;

    @BeforeEach
    void setUp() {
        pubChemConnector = new PubChemConnector();
    }

    @Test
    void getCID_returnsValidCid() throws IOException {
        String cid = pubChemConnector.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }

    @Test
    void connect_returnsValidJsonObject() throws IOException {
        JsonObject result = pubChemConnector.connector();

        assertNotNull(result);
        assertTrue(result.has("Record"));
    }

    @Test
    void setMedicine_changesMedicineUsedInRequest() throws IOException {
        pubChemConnector.setMedicine("paracetamol");

        String cid = pubChemConnector.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }
}