package viper.sentinox.control.feeder;

import viper.sentinox.control.BlueskyConnector;
import viper.sentinox.control.BlueskyGetToken;
import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.util.List;

public class BlueskyFeeder implements BlueskyFeederInteface {

    private final BlueskyGetToken getToken;
    private final BlueskyConnector connector;

    public BlueskyFeeder(BlueskyGetToken getToken) {
        this.getToken = getToken;
        this.connector = new BlueskyConnector();
    }

    public List<BlueskyEvent> get(String[] medicines,
                                  String token,
                                  String password)
            throws IOException, InterruptedException {

        String accessToken = getToken.getAccessToken(token, password);

        for (String medicine : medicines) {
            connector.setQuery(medicine);
        }
        return null;
    }
}