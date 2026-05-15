package viper.sentinox.control;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

public class LanguageClassifier {

    private final LanguageDetector detector;

    public LanguageClassifier() {
        detector = LanguageDetectorBuilder
                .fromLanguages(
                        Language.ENGLISH,
                        Language.SPANISH,
                        Language.FRENCH,
                        Language.GERMAN,
                        Language.ITALIAN,
                        Language.PORTUGUESE
                )
                .build();
    }

    public String detectLanguage(String text) {
        if (text == null || text.isBlank()) {
            return "Unknown";
        }

        Language language = detector.detectLanguageOf(text);

        return switch (language) {
            case ENGLISH -> "English";
            case SPANISH -> "Spanish";
            case FRENCH -> "French";
            case GERMAN -> "German";
            case ITALIAN -> "Italian";
            case PORTUGUESE -> "Portuguese";
            default -> "Unknown";
        };
    }
}
