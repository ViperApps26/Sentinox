import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.model.Comment;
import viper.sentinox.model.JointAnalysis;
import viper.sentinox.model.MedicineStats;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MedicineStatsTest {

    private MedicineStats stats;

    @BeforeEach
    void setUp() {
        stats = new MedicineStats();
    }

    @Test
    void addReaction_storesReaction() {
        stats.addReaction("Headache");

        assertEquals(1, stats.getReactions().size());
        assertEquals("Headache", stats.getReactions().get(0));
    }

    @Test
    void addReaction_ignoresBlankReaction() {
        stats.addReaction("");

        assertTrue(stats.getReactions().isEmpty());
    }

    @Test
    void addComment_storesComment() {
        Comment comment = new Comment(
                "author",
                "Ibuprofen helped me",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        stats.addComment(comment);

        assertEquals(1, stats.getComments().size());
        assertEquals("Ibuprofen helped me", stats.getComments().get(0).getText());
    }

    @Test
    void getCommentTexts_returnsOnlyTexts() {
        Comment comment = new Comment(
                "author",
                "Ibuprofen caused headache",
                "Negative",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        stats.addComment(comment);

        assertEquals(1, stats.getCommentTexts().size());
        assertEquals("Ibuprofen caused headache", stats.getCommentTexts().get(0));
    }

    @Test
    void defaultJointAnalysis_hasNoInformationConclusion() {
        JointAnalysis result = stats.getJointAnalysisResult();

        assertEquals(0, result.getMatchedReactions());
        assertEquals(0, result.getTotalReactions());
        assertEquals(0, result.getAgreementPercentage());
        assertTrue(result.getConclusion().contains("not enough information"));
    }
}
