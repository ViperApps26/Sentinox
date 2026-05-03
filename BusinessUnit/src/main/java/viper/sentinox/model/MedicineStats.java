package viper.sentinox.model;

import java.util.ArrayList;
import java.util.List;

public class MedicineStats {

    private int positive;
    private int negative;
    private int neutral;
    private final List<String> reactions;

    public MedicineStats() {
        this.positive = 0;
        this.negative = 0;
        this.neutral = 0;
        this.reactions = new ArrayList<>();
    }

    public void addSentiment(String sentiment) {
        if ("Positive".equalsIgnoreCase(sentiment)) {
            positive++;
        } else if ("Negative".equalsIgnoreCase(sentiment)) {
            negative++;
        } else {
            neutral++;
        }
    }

    public void addReaction(String reaction) {
        if (reaction != null && !reaction.isBlank()) {
            reactions.add(reaction);
        }
    }

    public String getSummary() {
        return """
                Positive opinions: %d
                Negative opinions: %d
                Neutral opinions: %d
                Total opinions: %d
                Reactions stored: %d
                """.formatted(
                positive,
                negative,
                neutral,
                positive + negative + neutral,
                reactions.size()
        );
    }
}