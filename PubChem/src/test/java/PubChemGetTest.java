import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;
import viper.sentinox.PubChemGet;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PubChemGetTest {

    private PubChemGet pubChemGet;

    @BeforeEach
    void setUp() {
        PubChemConnect pubChemConnect = new PubChemConnect("ibuprofen");
        pubChemGet = new PubChemGet(pubChemConnect);
    }

    @Test
    void getAllInfo_returnsRecordObject() throws IOException {
        JsonObject record = pubChemGet.getAllInfo();

        assertNotNull(record);
        assertTrue(record.has("Section"));
    }

    @Test
    void getReactions_returnsReactionList() throws IOException {
        List<String> reactions = pubChemGet.getReactions();

        assertNotNull(reactions);
        assertFalse(reactions.isEmpty());
    }

    @Test
    void getReactions_containsNonEmptyStrings() throws IOException {
        List<String> reactions = pubChemGet.getReactions();

        assertNotNull(reactions);
        assertFalse(reactions.isEmpty());

        for (String reaction : reactions) {
            assertNotNull(reaction);
            assertFalse(reaction.isBlank());
        }
    }
}//test//test