package viper.sentinox;

public class SentimentResult {

    private final int positive;
    private final int negative;
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
//test//test
    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }

    public int getNeutral() {
        return neutral;
    }

    public int getTotal() {
        return total;
    }

    public String getOverall() {
        return overall;
    }

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