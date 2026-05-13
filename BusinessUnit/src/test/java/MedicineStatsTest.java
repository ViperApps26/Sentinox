import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.model.Comment;
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
    void addSentiment_countsPositiveNegativeAndNeutral() {
        stats.addSentiment("Positive");
        stats.addSentiment("Negative");
        stats.addSentiment("Neutral");

        assertEquals(1, stats.getPositive());
        assertEquals(1, stats.getNegative());
        assertEquals(1, stats.getNeutral());
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
                "text",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        stats.addComment(comment);

        assertEquals(1, stats.getComments().size());
        assertEquals("text", stats.getComments().get(0).getText());
    }

    @Test
    void getCommentTexts_returnsOnlyTexts() {
        Comment comment = new Comment(
                "author",
                "Ibuprofen helped me",
                "Positive",
                Instant.parse("2026-05-01T10:00:00Z")
        );

        stats.addComment(comment);

        assertEquals(1, stats.getCommentTexts().size());
        assertEquals("Ibuprofen helped me", stats.getCommentTexts().get(0));
    }
}