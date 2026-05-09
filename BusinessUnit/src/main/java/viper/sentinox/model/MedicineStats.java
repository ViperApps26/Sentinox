package viper.sentinox.model;

import java.util.ArrayList;
import java.util.List;

public class MedicineStats {

    private int positive;
    private int negative;
    private int neutral;
    private final List<String> reactions;
    private final List<String> comments;

    public MedicineStats() {
        this.positive = 0;
        this.negative = 0;
        this.neutral = 0;
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void addSentiment(String sentiment) {
        switch (sentiment) {
            case "Positive" -> positive++;
            case "Negative" -> negative++;
            default -> neutral++;
        }
    }

    public void addComment(String comment) {
        if (comment != null && !comment.isBlank()) {
            comments.add(comment);
        }
    }

    public void addReaction(String reaction) {
        if (reaction != null && !reaction.isBlank()) {
            reactions.add(reaction);
        }
    }

    public String getMedicineSummary() {
        return """
                Positive opinions: %d
                Negative opinions: %d
                Neutral opinions: %d
                Total opinions: %d
                Reactions stored: %d
                Comments stored: %d
                """.formatted(
                positive,
                negative,
                neutral,
                positive + negative + neutral,
                reactions.size(),
                comments.size()
        );
    }
}