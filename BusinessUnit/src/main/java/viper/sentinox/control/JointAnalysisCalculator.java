package viper.sentinox.control;

import viper.sentinox.model.Comment;
import viper.sentinox.model.JointAnalysis;
import viper.sentinox.model.MedicineStats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JointAnalysisCalculator {

    private final Set<String> STOP_WORDS = new HashSet<>();
    private final Map<String, List<String>> MEDICAL_SYNONYMS = new HashMap<>();

    public JointAnalysisCalculator() throws IOException {
        loadStopWords();
        loadSynonyms();
    }

    public void loadStopWords() throws IOException {
        Files.readAllLines(Path.of("StopWords.txt"))
                .stream()
                .filter(line -> !line.isEmpty())
                .forEach(STOP_WORDS::add);
    }

    public void loadSynonyms() throws IOException {
        Files.readAllLines(Path.of("MedicinalSynonyms.txt"))
                .stream()
                .filter(line -> !line.isEmpty())
                .forEach(this::addSynonyms);
    }

    private void addSynonyms(String line) {
        String[] parts = line.split("=");
        String key = parts[0];

        List<String> values = Arrays.stream(parts[1].split(",")).toList();

        MEDICAL_SYNONYMS.put(key, values);
    }


    public JointAnalysis analyze(MedicineStats stats) {
        List<String> reactions = stats.getReactions();
        List<Comment> comments = stats.getComments();
        int matched = countMatches(reactions, comments);

        double percentage = matched * 100.0 / reactions.size();

        return new JointAnalysis(
                matched,
                reactions.size(),
                percentage,
                buildConclusion(percentage)
        );
    }

    private int countMatches(List<String> reactions, List<Comment> comments) {
        int matched = 0;

        for (String reaction : reactions) {
            if (matchesAnyComment(reaction, comments)) {
                matched++;
            }
        }
        return matched;
    }

    private boolean matchesAnyComment(String reaction, List<Comment> comments) {
        Set<String> reactionWords = extractKeywords(reaction);
        int minMatches = reactionWords.size() > 4 ? 2 : 1;

        for (Comment comment : comments) {
            Set<String> commentWords = extractKeywords(comment.getText());

            int score = calculateSimilarity(reactionWords, commentWords);
            if (score >= minMatches) {
                return true;
            }
        }
        return false;
    }

    private int calculateSimilarity(Set<String> reactionWords, Set<String> commentWords) {
        int score = 0;

        for (String reactionWord : reactionWords) {
            if (commentWords.contains(reactionWord)) {
                score++;
                continue;
            }
            if (matchesSynonym(commentWords, reactionWord)) {
                score++;
            }
        }
        return score;
    }

    private boolean matchesSynonym(Set<String> commentWords, String reactionWord) {
        List<String> synonyms = MEDICAL_SYNONYMS.get(reactionWord);

        if (synonyms != null) {
            for (String synonym : synonyms) {
                if (commentWords.contains(synonym)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<String> extractKeywords(String text) {
        return Arrays.stream(
                        normalize(text).split("\\W+")
                )
                .map(this::simplifyWord)
                .filter(word -> word.length() > 3)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.toSet());
    }

    private String normalize(String text) {
        return text.toLowerCase().trim();
    }

    private String simplifyWord(String word) {
        if (word.endsWith("ies") && word.length() > 4) {
            return getVerbBase(word, 3) + "y";
        }
        if (word.endsWith("ing") && word.length() > 5) {
            return getVerbBase(word, 3);
        }
        if (word.endsWith("es") && word.length() > 4) {
            return getVerbBase(word, 2);
        }
        if (word.endsWith("s") && word.length() > 4) {
            return getVerbBase(word, 1);
        }
        return word;
    }

    private static String getVerbBase(String word, int termination) {
        return word.substring(0, word.length() - termination);
    }


    private String buildConclusion(double percentage) {
        if (percentage >= 60) {
            return "High agreement: User comments frequently mention known adverse reactions.";
        }
        if (percentage >= 30) {
            return "Moderate agreement: Some user comments match known adverse reactions.";
        }
        return "Low agreement: User comments do not strongly match known adverse reactions.";
    }
}