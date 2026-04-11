import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PubChemConnectTest {

    private PubChemConnect pubChemConnect;

    @BeforeEach
    void setUp() {
        pubChemConnect = new PubChemConnect("ibuprofen");
    }

    @Test
    void getCID_returnsValidCid() throws IOException {
        String cid = pubChemConnect.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }

    @Test
    void connect_returnsValidJsonObject() throws IOException {
        JsonObject result = pubChemConnect.connect();

        assertNotNull(result);
        assertTrue(result.has("Record"));
    }

    @Test
    void setMedicine_changesMedicineUsedInRequest() throws IOException {
        pubChemConnect.setMedicine("paracetamol");

        String cid = pubChemConnect.getCID();

        assertNotNull(cid);
        assertFalse(cid.isBlank());
        assertTrue(cid.matches("\\d+"));
    }
}//test