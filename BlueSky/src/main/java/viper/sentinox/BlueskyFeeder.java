package viper.sentinox;

import java.io.IOException;

public class BlueskyFeeder {

    private final BlueskyGetToken blueskyGetToken;
    private final BlueskyInsert blueskyInsert;

    public BlueskyFeeder(BlueskyGetToken blueskyGetToken, BlueskyInsert blueskyInsert) {
        this.blueskyGetToken = blueskyGetToken;
        this.blueskyInsert = blueskyInsert;
    }

    public void feedCurrentQuery(String token, String password, String databaseURL)
            throws IOException, InterruptedException {

        String accessToken = blueskyGetToken.getAccessToken(token, password);
        blueskyInsert.savePosts(accessToken, databaseURL);
        System.out.println("Bluesky data stored correctly");
    }
}