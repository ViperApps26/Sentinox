package viper.sentinox.model;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMedicineDataMart implements DataMart {

    private final Map<String, MedicineStats> medicineStats;

    public InMemoryMedicineDataMart() {
        this.medicineStats = new HashMap<>();
    }

    @Override
    public synchronized void registerSentiment(String medicine, String sentiment) {
        MedicineStats stats = getOrCreateStats(medicine);
        stats.addSentiment(sentiment);
    }

    @Override
    public synchronized void registerReaction(String medicine, String reaction) {
        MedicineStats stats = getOrCreateStats(medicine);
        stats.addReaction(reaction);
    }

    @Override
    public synchronized String getSummary() {
        if (medicineStats.isEmpty()) {
            return "No data registered yet.";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("\n========== DATAMART SUMMARY ==========\n");

        for (Map.Entry<String, MedicineStats> entry : medicineStats.entrySet()) {
            summary.append("\nMedicine: ")
                    .append(entry.getKey())
                    .append("\n")
                    .append(entry.getValue().getSummary());
        }

        summary.append("======================================\n");
        return summary.toString();
    }

    private MedicineStats getOrCreateStats(String medicine) {
        return medicineStats.computeIfAbsent(medicine, key -> new MedicineStats());
    }
}