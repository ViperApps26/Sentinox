package viper.sentinox.model;

import java.util.ArrayList;
import java.util.List;

public class MedicineStats {

    private final List<String> reactions;
    private final List<Comment> comments;

    private JointAnalysisResult jointAnalysisResult;

    public MedicineStats() {
        this.reactions = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.jointAnalysisResult = new JointAnalysisResult(
                0,
                0,
                0,
                "There is not enough information to compare user comments and known adverse reactions."
        );
    }

    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
        }
    }

    public void addReaction(String reaction) {
        if (reaction != null && !reaction.isBlank()) {
            reactions.add(reaction);
        }
    }

    public List<String> getReactions() {
        return reactions;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getCommentTexts() {
        return comments.stream()
                .map(Comment::getText)
                .toList();
    }

    public JointAnalysisResult getJointAnalysisResult() {
        return jointAnalysisResult;
    }

    public void setJointAnalysisResult(JointAnalysisResult jointAnalysisResult) {
        this.jointAnalysisResult = jointAnalysisResult;
    }
}