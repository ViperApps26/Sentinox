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
                Comments stored: %d
                Positive opinions: %d
                Negative opinions: %d
                Neutral opinions: %d
                Reactions stored: %d
                """.formatted(
                comments.size(),
                positive,
                negative,
                neutral,
                reactions.size()

        );
    }

    public String getReactionsSummary() {
        if (reactions.isEmpty()) {
            return "No reactions recorded.";
        }
        StringBuilder sb = new StringBuilder("Reactions:\n");
        for (String reaction : reactions) {
            sb.append("  * ").append(reaction).append("\n");
        }
        return sb.toString();
    }

    public String getCommentsSummary() {
        if (comments.isEmpty()) {
            return "No comments recorded.";
        }
        StringBuilder sb = new StringBuilder("Comments:\n");
        for (String comment : comments) {
            sb.append("  * ").append(comment).append("\n");
        }
        return sb.toString();
    }

    public String getSentimentSummary() {
        return """
                Positive: %d
                Negative: %d
                Neutral: %d
                """.formatted(positive, negative, neutral);
    }

}