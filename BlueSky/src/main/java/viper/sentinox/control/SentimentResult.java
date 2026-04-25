package viper.sentinox.control;

public class SentimentResult {
    private final String overall;

    public SentimentResult(String overall) {
        this.overall = overall;
    }

    public String getOverall() { return overall; }
}