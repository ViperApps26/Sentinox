package viper.sentinox;

import viper.sentinox.control.BlueskyControl;
import viper.sentinox.control.BlueskyGetToken;
import viper.sentinox.control.feeder.BlueskyFeeder;
import viper.sentinox.control.store.ActiveMQBlueskyStore;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Use: java viper.sentinox.Main <refreshToken> <password> <ActiveMQUrl> <topicName>");
            return;
        }
        String token = args[0];
        String password = args[1];
        String url = args[2];
        String topic = args[3];

        BlueskyControl control = createBlueskyEnvironment(url, topic, token, password);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, control);
    }

    private static void autoExecute(ScheduledExecutorService scheduler,
                                    BlueskyControl control) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static BlueskyControl createBlueskyEnvironment(String url, String topic, String token, String password) {
        BlueskyGetToken getToken = new BlueskyGetToken(token, password);

        ActiveMQBlueskyStore store = new ActiveMQBlueskyStore(url, topic);
        BlueskyFeeder feeder = new BlueskyFeeder(getToken);

        return new BlueskyControl(feeder, store);
    }
}