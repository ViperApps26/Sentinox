package viper.sentinox.model;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class MedicineDataMart {

    private final Map<String, MedicineStats> statsByMedicine;

    public MedicineDataMart() {
        this.statsByMedicine = new HashMap<>();
    }

    public synchronized void registerBlueskyEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String sentiment = event.get("sentiment").getAsString();

        MedicineStats stats = getOrCreateStats(medicine);
        stats.addSentiment(sentiment);
    }

    public synchronized void registerPubChemEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String reaction = event.get("reaction").getAsString();

        MedicineStats stats = getOrCreateStats(medicine);
        stats.addReaction(reaction);
    }

    public synchronized String getSummary() {
        if (statsByMedicine.isEmpty()) {
            return """
                    
                    ========== DATAMART SUMMARY ==========
                    No data registered yet.
                    ======================================
                    """;
        }

        StringBuilder summary = new StringBuilder();
        summary.append("\n========== DATAMART SUMMARY ==========\n");

        for (Map.Entry<String, MedicineStats> entry : statsByMedicine.entrySet()) {
            summary.append("\nMedicine: ")
                    .append(entry.getKey())
                    .append("\n")
                    .append(entry.getValue().getSummary());
        }

        summary.append("======================================");

        return summary.toString();
    }

    private MedicineStats getOrCreateStats(String medicine) {
        return statsByMedicine.computeIfAbsent(medicine, key -> new MedicineStats());
    }
}