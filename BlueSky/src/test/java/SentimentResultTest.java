import org.junit.jupiter.api.Test;
import viper.sentinox.control.sentimentanalysis.SentimentResult;

import static org.junit.jupiter.api.Assertions.*;

class SentimentResultTest {

    @Test
    void getOverall_returnsCorrectSentiment() {

        SentimentResult result = new SentimentResult("Positive");

        assertEquals("Positive", result.getOverall());
    }
}