package viper.sentinox.control;

import viper.sentinox.model.BlueskyConnect;
import viper.sentinox.model.BlueskyGetToken;

import java.io.IOException;

public class BlueskyFeeder implements BlueskyFeederInteface {

    private final BlueskyGetToken getToken;
    private final BlueskyConnect connect;
    private final BlueskyPublisher publisher;

    public BlueskyFeeder(BlueskyGetToken getToken,
                         BlueskyConnect connect,
                         BlueskyPublisher publisher) {
        this.getToken = getToken;
        this.connect = connect;
        this.publisher = publisher;
    }

    public void feedMedicinesFromList(String[] medicines,
                                      String token,
                                      String password)
            throws IOException, InterruptedException {

        String accessToken = getToken.getAccessToken(token, password);

        for (String medicine : medicines) {
            connect.setQuery(medicine);
            publisher.publishPosts(accessToken);
        }
    }
}