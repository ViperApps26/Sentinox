package viper.sentinox.model.feeder;

import com.google.gson.JsonArray;
import viper.sentinox.model.BlueskyConnector;
import viper.sentinox.model.BlueskyGet;
import viper.sentinox.model.BlueskyGetToken;
import viper.sentinox.model.SentimentAnalysis;
import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyFeeder implements BlueskyGetEvents {

    private final BlueskyGetToken getToken;
    private final BlueskyConnector connector;
    private final BlueskyGet get;
    private final SentimentAnalysis sentimentAnalysis;

    public BlueskyFeeder(BlueskyGetToken getToken) {
        this.getToken = getToken;
        this.connector = new BlueskyConnector();
        this.get = new BlueskyGet(connector);
        this.sentimentAnalysis = new SentimentAnalysis();
    }

    public List<BlueskyEvent> get(String medicine) throws IOException, InterruptedException {
        String token = getToken.getAccessToken();
        connector.setQuery(medicine);

        return getEvents(token);
    }

    public List<BlueskyEvent> getEvents(String token) throws IOException {
        JsonArray postAttributes = get.getPostsAttributes(token);

        List<String> authors = get.getAuthors(postAttributes);
        List<String> posts = get.getPosts(postAttributes);
        List<String> creationDates = get.getCreationDates(postAttributes);

        List<BlueskyEvent> events = new ArrayList<>();
        addEvents(posts, events, authors, creationDates);
        return events;
    }

    private void addEvents(List<String> posts, List<BlueskyEvent> events, List<String> authors, List<String> creationDates) {
        for (int i = 0; i < posts.size(); i++) {
            String text = posts.get(i);

            events.add(new BlueskyEvent(
                    System.currentTimeMillis(),
                    "BlueskyFeeder",
                    connector.getQuery(),
                    authors.get(i),
                    text,
                    sentimentAnalysis.analyze(text).getOverall(),
                    creationDates.get(i)
            ));
        }
    }
}