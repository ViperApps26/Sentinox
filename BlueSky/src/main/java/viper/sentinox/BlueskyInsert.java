package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

public class BlueskyInsert {

    private final BlueskyConnect blueskyConnect;
    private final BlueskyGet blueskyGet;
    private final SentimentAnalysis sentimentAnalysis;
    private final BlueskyPublisher blueskyPublisher;
    private final Gson gson;

    public BlueskyInsert(BlueskyConnect blueskyConnect,
                         BlueskyGet blueskyGet,
                         SentimentAnalysis sentimentAnalysis) {
        this.blueskyConnect = blueskyConnect;
        this.blueskyGet = blueskyGet;
        this.sentimentAnalysis = sentimentAnalysis;
        this.blueskyPublisher = new BlueskyPublisher();
        this.gson = new Gson();
    }

    public void savePosts(String token, String databaseURL) throws IOException {
        JsonArray postAttributes = blueskyGet.getPostsAttributes(token);

        List<String> authors = blueskyGet.getAuthors(postAttributes);
        List<String> posts = blueskyGet.getPosts(postAttributes);
        List<String> creationDates = blueskyGet.getCreationDates(postAttributes);

        if (posts.isEmpty()) {
            System.out.println(blueskyConnect.getQuery() + " has no posts to publish");
            return;
        }

        publishBlueskyPosts(
                blueskyConnect.getQuery(),
                authors,
                posts,
                creationDates
        );

        System.out.println(blueskyConnect.getQuery() + " posts published correctly");
    }

    private void publishBlueskyPosts(String medicine,
                                     List<String> authors,
                                     List<String> posts,
                                     List<String> creationDates) {

        for (int i = 0; i < posts.size(); i++) {
            String text = posts.get(i);
            String sentiment = sentimentAnalysis.analyze(text).getOverall();

            BlueskyEvent event = new BlueskyEvent(
                    System.currentTimeMillis(),
                    "BlueskyFeeder",
                    medicine,
                    authors.get(i),
                    text,
                    sentiment,
                    creationDates.get(i)
            );

            String json = gson.toJson(event);
            blueskyPublisher.publish(json);
        }
    }
}