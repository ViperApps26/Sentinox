package viper.sentinox.control;

import viper.sentinox.model.Comment;
import viper.sentinox.model.JointAnalysis;
import viper.sentinox.model.MedicineStats;

import java.util.List;

public class JointAnalysisCalculator {

    public JointAnalysis analyze(MedicineStats stats) {
        List<String> reactions = stats.getReactions();
        List<Comment> comments = stats.getComments();

        if (reactions.isEmpty() || comments.isEmpty()) {
            return new JointAnalysis(
                    0,
                    reactions.size(),
                    0,
                    "There is not enough information to compare user comments and known adverse reactions."
            );
        }

        int matchedReactions = countMatchedReactions(reactions, comments);
        double percentage = calculatePercentage(matchedReactions, reactions.size());

        return new JointAnalysis(
                matchedReactions,
                reactions.size(),
                percentage,
                buildConclusion(percentage)
        );
    }

    private int countMatchedReactions(List<String> reactions, List<Comment> comments) {
        int matched = 0;

        for (String reaction : reactions) {
            if (isReactionMentionedInComments(reaction, comments)) {
                matched++;
            }
        }

        return matched;
    }

    private boolean isReactionMentionedInComments(String reaction, List<Comment> comments) {
        String normalizedReaction = normalize(reaction);

        for (Comment comment : comments) {
            String normalizedComment = normalize(comment.getText());

            if (normalizedComment.contains(normalizedReaction)) {
                return true;
            }

            if (containsRelevantWord(normalizedReaction, normalizedComment)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsRelevantWord(String reaction, String comment) {
        String[] words = reaction.split("\\W+");

        for (String word : words) {
            if (word.length() > 4 && comment.contains(word)) {
                return true;
            }
        }

        return false;
    }

    private String normalize(String text) {
        return text == null ? "" : text.toLowerCase().trim();
    }

    private double calculatePercentage(int matched, int total) {
        if (total == 0) {
            return 0;
        }

        return (matched * 100.0) / total;
    }

    private String buildConclusion(double percentage) {
        if (percentage >= 60) {
            return "High agreement: User comments frequently mention known adverse reactions.";
        } else if (percentage >= 30) {
            return "Moderate agreement: Some user comments match known adverse reactions.";
        } else {
            return "Low agreement: User comments do not strongly match known adverse reactions.";
        }
    }
}