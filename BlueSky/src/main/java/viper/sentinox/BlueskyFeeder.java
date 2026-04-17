package viper.sentinox;

import java.io.IOException;

public class BlueskyFeeder {

    private final BlueskyGetToken blueskyGetToken;
    private final BlueskyInsert blueskyInsert;
    private final BlueskyConnect blueskyConnect;

    public BlueskyFeeder(BlueskyGetToken blueskyGetToken,
                         BlueskyInsert blueskyInsert,
                         BlueskyConnect blueskyConnect) {
        this.blueskyGetToken = blueskyGetToken;
        this.blueskyInsert = blueskyInsert;
        this.blueskyConnect = blueskyConnect;
    }

    public void feedMedicinesFromList(String[] medicines,
                                      String token,
                                      String password,
                                      String databaseURL)
            throws IOException, InterruptedException {

        String accessToken = blueskyGetToken.getAccessToken(token, password);

        for (String medicine : medicines) {
            blueskyConnect.setQuery(medicine);
            blueskyInsert.savePosts(accessToken, databaseURL);
        }
    }
}