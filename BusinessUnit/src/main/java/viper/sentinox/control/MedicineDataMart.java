package viper.sentinox.control;

import viper.sentinox.model.Comment;
import viper.sentinox.model.DataMart;
import viper.sentinox.model.JointAnalysisCalculator;
import viper.sentinox.model.JointAnalysisResult;
import viper.sentinox.model.MedicineStats;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineDataMart implements DataMart {

    private final Map<String, MedicineStats> medicineStatsMap;
    private final JointAnalysisCalculator jointAnalysisCalculator;

    public MedicineDataMart() {
        this.medicineStatsMap = new HashMap<>();
        this.jointAnalysisCalculator = new JointAnalysisCalculator();
    }

    @Override
    public synchronized void registerBlueskyEvent(String medicine,
                                                  String author,
                                                  String text,
                                                  String sentiment,
                                                  Instant date) {
        MedicineStats stats = getOrCreateStats(medicine);
        Comment comment = new Comment(author, text, sentiment, date);

        if (!stats.getCommentTexts().contains(comment.getText())) {
            stats.addComment(comment);
            stats.addSentiment(sentiment);
            updateJointAnalysis(stats);
        }
    }

    @Override
    public synchronized void registerPubChemEvent(String medicine, String reaction) {
        MedicineStats stats = getOrCreateStats(medicine);

        if (!stats.getReactions().contains(reaction)) {
            stats.addReaction(reaction);
            updateJointAnalysis(stats);
        }
    }

    private MedicineStats getOrCreateStats(String medicine) {
        return medicineStatsMap.computeIfAbsent(medicine, key -> new MedicineStats());
    }

    private void updateJointAnalysis(MedicineStats stats) {
        JointAnalysisResult result = jointAnalysisCalculator.analyze(stats);
        stats.setJointAnalysisResult(result);
    }

    public synchronized List<String> getMedicineReactions(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return List.of();
        }

        return stats.getReactions();
    }

    public synchronized List<Comment> getMedicineComments(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return List.of();
        }

        return stats.getComments();
    }

    public synchronized int getMedicineSentimentPositive(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return 0;
        }

        return stats.getPositive();
    }

    public synchronized int getMedicineSentimentNegative(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return 0;
        }

        return stats.getNegative();
    }

    public synchronized int getMedicineSentimentNeutral(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return 0;
        }

        return stats.getNeutral();
    }

    public synchronized JointAnalysisResult getMedicineJointAnalysis(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);

        if (stats == null) {
            return new JointAnalysisResult(
                    0,
                    0,
                    0,
                    "There is no information available for this medicine."
            );
        }

        return stats.getJointAnalysisResult();
    }

    public Map<String, MedicineStats> getAllStats() {
        return medicineStatsMap;
    }
}