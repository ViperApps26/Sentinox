package viper.sentinox.model;

public class JointAnalysis {

    private final int matchedReactions;
    private final int totalReactions;
    private final double agreementPercentage;
    private final String conclusion;

    public JointAnalysis(int matchedReactions,
                         int totalReactions,
                         double agreementPercentage,
                         String conclusion) {
        this.matchedReactions = matchedReactions;
        this.totalReactions = totalReactions;
        this.agreementPercentage = agreementPercentage;
        this.conclusion = conclusion;
    }

    public int getMatchedReactions() {
        return matchedReactions;
    }

    public int getTotalReactions() {
        return totalReactions;
    }

    public double getAgreementPercentage() {
        return agreementPercentage;
    }

    public String getConclusion() {
        return conclusion;
    }
}