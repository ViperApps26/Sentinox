import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.feeder.PubChemConnector;
import viper.sentinox.control.feeder.PubChemGet;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PubChemGetTest {

    private PubChemGet pubChemGet;

    @BeforeEach
    void setUp() {
        PubChemConnector connector = new FakePubChemConnector();
        pubChemGet = new PubChemGet(connector);
    }

    @Test
    void getAllInfo_returnsRecordObject() throws IOException {
        JsonObject result = pubChemGet.getAllInfo();

        assertNotNull(result);
        assertTrue(result.has("Section"));
    }

    @Test
    void getReactions_returnsAdverseEffects() throws IOException {
        ArrayList<String> reactions = pubChemGet.getReactions();

        assertNotNull(reactions);
        assertEquals(2, reactions.size());
        assertEquals("Headache", reactions.get(0));
        assertEquals("Nausea", reactions.get(1));
    }

    private static class FakePubChemConnector extends PubChemConnector {

        @Override
        public JsonObject connector() {
            String response = """
                    {
                      "Record": {
                        "Section": [
                          {
                            "TOCHeading": "Clinical Information",
                            "Section": [
                              {
                                "TOCHeading": "Adverse Effects",
                                "Information": [
                                  {
                                    "Value": {
                                      "StringWithMarkup": [
                                        {
                                          "String": "Headache"
                                        },
                                        {
                                          "String": "Nausea"
                                        }
                                      ]
                                    }
                                  }
                                ]
                              }
                            ]
                          }
                        ]
                      }
                    }
                    """;

            return new Gson().fromJson(response, JsonObject.class);
        }
    }
}