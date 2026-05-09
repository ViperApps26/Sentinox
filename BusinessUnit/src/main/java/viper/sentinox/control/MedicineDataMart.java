package viper.sentinox.control;

import viper.sentinox.model.DataMart;
import viper.sentinox.model.MedicineStats;

import java.util.HashMap;
import java.util.Map;

public class MedicineDataMart implements DataMart {

    private final Map<String, MedicineStats> medicineStatsMap;

    public MedicineDataMart() {
        this.medicineStatsMap = new HashMap<>();
    }


    @Override
    public synchronized void registerBlueskyEvent(String medicine, String comment, String sentiment) {
        MedicineStats stats = getOrCreateStats(medicine);
        stats.addComment(comment);
        stats.addSentiment(sentiment);
    }

    @Override
    public synchronized void registerPubChemEvent(String medicine, String reaction) {
        MedicineStats stats = getOrCreateStats(medicine);
        stats.addReaction(reaction);
    }

    private MedicineStats getOrCreateStats(String medicine) {
        return medicineStatsMap.computeIfAbsent(medicine, key -> new MedicineStats());
    }

    public synchronized String getMedicinesSummary() {
        if (medicineStatsMap.isEmpty()) {
            return "No data registered yet.";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("\n========== DATAMART SUMMARY ==========\n");

        for (Map.Entry<String, MedicineStats> entry : medicineStatsMap.entrySet()) {
            summary.append("\nMedicine: ")
                    .append(entry.getKey())
                    .append("\n")
                    .append(entry.getValue().getMedicineSummary());
        }
        summary.append("======================================\n");
        return summary.toString();
    }

    public synchronized String getMedicineDetails(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);
        if (stats == null) return "Medicine not found.";
        return stats.getMedicineSummary();
    }

    public synchronized String getMedicineReactions(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);
        if (stats == null) return "Medicine not found.";
        return stats.getReactionsSummary();
    }

    public synchronized String getMedicineComments(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);
        if (stats == null) return "Medicine not found.";
        return stats.getCommentsSummary();
    }

    public synchronized String getMedicineSentiment(String medicine) {
        MedicineStats stats = medicineStatsMap.get(medicine);
        if (stats == null) return "Medicine not found.";
        return stats.getSentimentSummary();
    }

}