import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.JointAnalysisCalculator;
import viper.sentinox.model.Comment;
import viper.sentinox.model.JointAnalysis;
import viper.sentinox.model.MedicineStats;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JointAnalysisCalculatorTest {

    private JointAnalysisCalculator calculator;

    @BeforeEach
    void setUp() throws IOException {
        calculator = new JointAnalysisCalculator();
    }

    @Test
    void analyze_returnsHighAgreementWhenReactionsAppearInComments() {
        MedicineStats stats = new MedicineStats();

        stats.addReaction("headache");
        stats.addReaction("nausea");

        stats.addComment(new Comment(
                "user1",
                "This medicine caused headache and nausea",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        ));

        JointAnalysis result = calculator.analyze(stats);

        assertEquals(2, result.getMatchedReactions());
        assertEquals(2, result.getTotalReactions());
        assertEquals(100.0, result.getAgreementPercentage());
        assertTrue(result.getConclusion().contains("High agreement"));
    }

    @Test
    void analyze_matchesSynonyms() {
        MedicineStats stats = new MedicineStats();

        stats.addReaction("headache");

        stats.addComment(new Comment(
                "user1",
                "I had migraine after taking it",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        ));

        JointAnalysis result = calculator.analyze(stats);

        assertEquals(1, result.getMatchedReactions());
        assertEquals(100.0, result.getAgreementPercentage());
    }

    @Test
    void analyze_returnsLowAgreementWhenNoReactionsMatch() {
        MedicineStats stats = new MedicineStats();

        stats.addReaction("headache");
        stats.addReaction("nausea");

        stats.addComment(new Comment(
                "user1",
                "This medicine worked perfectly",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        ));

        JointAnalysis result = calculator.analyze(stats);

        assertEquals(0, result.getMatchedReactions());
        assertEquals(2, result.getTotalReactions());
        assertEquals(0.0, result.getAgreementPercentage());
        assertTrue(result.getConclusion().contains("Low agreement"));
    }
}
