
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PubChemConnectTest {

    @BeforeEach
    void setUp() {
        PubChemConnect.setMedicine("ibuprofen");
    }

    @Test
    void getCID_returnsValidCid() throws IOException {
        String cid = PubChemConnect.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }

    @Test
    void connect_returnsValidJsonObject() throws IOException {
        JsonObject result = PubChemConnect.connect();

        assertNotNull(result);
        assertTrue(result.has("Record"));
    }

    @Test
    void setMedicine_changesMedicineUsedInRequest() throws IOException {
        PubChemConnect.setMedicine("paracetamol");

        String cid = PubChemConnect.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }
}

