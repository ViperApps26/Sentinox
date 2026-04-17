package viper.sentinox;

import java.io.IOException;

public class BlueskyControl {

    private final BlueskyFeeder blueskyFeeder;

    public BlueskyControl(BlueskyFeeder blueskyFeeder) {
        this.blueskyFeeder = blueskyFeeder;
    }

    public void execute(String token, String password, String databaseURL)
            throws IOException, InterruptedException {
        blueskyFeeder.feedMedicines(token, password, databaseURL);
    }
}