package viper.sentinox;

import java.util.Properties;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class SentimentAnalysis {

    private final StanfordCoreNLP pipeline;

    public SentimentAnalysis() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public SentimentResult analyze(String text) {
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);

        int positive = 0;
        int negative = 0;
        int neutral = 0;

        for (CoreSentence sentence : doc.sentences()) {
            String sentiment = sentence.sentiment();

            switch (sentiment) {
                case "Positive":
                case "Very Positive":
                    positive++;
                    break;
                case "Negative":
                case "Very Negative":
                    negative++;
                    break;
                default:
                    neutral++;
                    break;
            }
        }

        String overall = calculateOverallSentiment(positive, negative, neutral);

        return new SentimentResult(positive, negative, neutral, overall);
    }

    private String calculateOverallSentiment(int positive, int negative, int neutral) {
        if (positive > negative && positive > neutral) {
            return "Positive";
        } else if (negative > positive && negative > neutral) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }
}