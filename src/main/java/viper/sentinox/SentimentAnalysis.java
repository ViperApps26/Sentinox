package viper.sentinox;


import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import java.util.Properties;


public class SentimentAnalysis {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,parse,sentiment");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String text = "I really love the product, but the service was slow. "
                + "Overall, I think the experience could be improved, but it was not terrible.";

        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);


        int pos = 0, neg = 0, neu = 0;
        System.out.println("=== Sentence-level analysis ===");

        for (CoreSentence sentence : doc.sentences()) {
            String sentiment = sentence.sentiment();
            System.out.println(sentence.text() + " -> " + sentiment);

            switch (sentiment) {
                case "Positive":
                case "Very Positive":
                    pos++;
                    break;
                case "Negative":
                case "Very Negative":
                    neg++;
                    break;
                default:
                    neu++;
            }
        }

        int total = pos + neg + neu;

        System.out.println("\n=== Global statistics ===");
        System.out.println("Positive: " + pos + " (" + (pos * 100 / total) + "%)");
        System.out.println("Negative: " + neg + " (" + (neg * 100 / total) + "%)");
        System.out.println("Neutral: " + neu + " (" + (neu * 100 / total) + "%)");

        String overall;
        if (pos > neg && pos > neu) overall = "Positive";
        else if (neg > pos && neg > neu) overall = "Negative";
        else overall = "Neutral";

        System.out.println("\nOverall sentiment: " + overall);

    }
}
