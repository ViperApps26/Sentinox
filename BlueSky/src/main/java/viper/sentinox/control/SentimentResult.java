package viper.sentinox.control;

public class SentimentResult implements SentimentResultInterface{

    private final int negative;
    private final int positive;
    private final int neutral;
    private final int total;
    private final String overall;

    public SentimentResult(int positive, int negative, int neutral, String overall) {
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
        this.total = positive + negative + neutral;
        this.overall = overall;
    }

    public String getOverall() { return overall; }

    public int getPositivePercentage() {
        return total == 0 ? 0 : (positive * 100 / total);
    }

    public int getNegativePercentage() {
        return total == 0 ? 0 : (negative * 100 / total);
    }

    public int getNeutralPercentage() {
        return total == 0 ? 0 : (neutral * 100 / total);
    }
}