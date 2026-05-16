import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.LanguageClassifier;

import static org.junit.jupiter.api.Assertions.*;

class LanguageClassifierTest {

    private LanguageClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new LanguageClassifier();
    }

    @Test
    void detectLanguage_returnsUnknownWhenTextIsBlank() {
        assertEquals("Unknown", classifier.detectLanguage(""));
    }

    @Test
    void detectLanguage_returnsEnglish() {
        assertEquals(
                "English",
                classifier.detectLanguage("Ibuprofen gave me a headache yesterday")
        );
    }

    @Test
    void detectLanguage_returnsSpanish() {
        assertEquals(
                "Spanish",
                classifier.detectLanguage("El ibuprofeno me causó dolor de cabeza")
        );
    }
}
