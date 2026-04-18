package viper.sentinox;

import viper.sentinox.control.BlueskyControl;
import viper.sentinox.control.BlueskyFeeder;
import viper.sentinox.control.BlueskyPublisher;
import viper.sentinox.model.BlueskyConnect;
import viper.sentinox.model.BlueskyGet;
import viper.sentinox.model.BlueskyGetToken;
import viper.sentinox.model.SentimentAnalysis;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Use: java viper.sentinox.Main <refreshToken> <password>");
            return;
        }
        String token = args[0];
        String password = args[1];

        BlueskyControl control = createBlueskyEnvironment();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, control, token, password);
    }

    private static void autoExecute(ScheduledExecutorService scheduler,
                                    BlueskyControl control,
                                    String token,
                                    String password) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute(token, password);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static BlueskyControl createBlueskyEnvironment() {
        BlueskyGetToken getToken = new BlueskyGetToken();
        BlueskyConnect connect = new BlueskyConnect();
        BlueskyGet get = new BlueskyGet(connect);
        SentimentAnalysis sentiment = new SentimentAnalysis();
        BlueskyPublisher publisher = new BlueskyPublisher(connect, get, sentiment);
        BlueskyFeeder feeder = new BlueskyFeeder(getToken, connect, publisher);

        return new BlueskyControl(feeder);
    }
}