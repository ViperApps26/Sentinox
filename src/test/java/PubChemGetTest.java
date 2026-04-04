import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;
import viper.sentinox.PubChemGet;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PubChemGetTest {

    @BeforeEach
    void setUp() {
        PubChemConnect.setMedicine("ibuprofen");
    }

    @Test
    void getAllInfo_returnsRecordObject() throws IOException {
        JsonObject record = PubChemGet.getAllInfo();

        assertNotNull(record);
        assertTrue(record.has("Section"));
    }

    @Test
    void getReactions_returnsReactionList() throws IOException {
        ArrayList<String> reactions = PubChemGet.getReactions();

        assertNotNull(reactions);
        assertFalse(reactions.isEmpty());
    }

    @Test
    void getReactions_containsNonEmptyStrings() throws IOException {
        ArrayList<String> reactions = PubChemGet.getReactions();

        assertNotNull(reactions);
        assertFalse(reactions.isEmpty());

        for (String reaction : reactions) {
            assertNotNull(reaction);
            assertFalse(reaction.isBlank());
        }
    }
}






