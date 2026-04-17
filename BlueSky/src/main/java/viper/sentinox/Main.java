package viper.sentinox;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String token = args[0];
        String password = args[1];
        String databaseURL = args[2];

        BlueskyControl blueskyControl = createBlueskyEnvironment();
        createDatabase(databaseURL);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, blueskyControl, token, password, databaseURL);
    }

    private static void autoExecute(ScheduledExecutorService scheduler,
                                    BlueskyControl blueskyControl,
                                    String token,
                                    String password,
                                    String databaseURL) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                blueskyControl.execute(token, password, databaseURL);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static BlueskyControl createBlueskyEnvironment() {
        BlueskyGetToken blueskyGetToken = new BlueskyGetToken();
        BlueskyConnect blueskyConnect = new BlueskyConnect();
        BlueskyGet blueskyGet = new BlueskyGet(blueskyConnect);
        BlueskyInsert blueskyInsert = new BlueskyInsert(blueskyConnect, blueskyGet);
        BlueskyFeeder blueskyFeeder = new BlueskyFeeder(blueskyGetToken, blueskyInsert, blueskyConnect);

        return new BlueskyControl(blueskyFeeder);
    }

    private static void createDatabase(String databaseURL) {
        BlueskyDatabaseCreator blueskyDatabaseCreator = new BlueskyDatabaseCreator();
        blueskyDatabaseCreator.createDatabase(databaseURL);
    }
}