package viper.sentinox;

import viper.sentinox.control.BlueskyController;
import viper.sentinox.control.oauth.BlueskyGetToken;
import viper.sentinox.control.feeder.BlueskyFeeder;
import viper.sentinox.control.store.ActiveMQBlueskyStore;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 6) {
            log.error("Use: java viper.sentinox.Main <refreshToken> <user> <password> <ActiveMQUrl> <topicName> <medicinesListPath>");
            return;
        }
        String refreshToken = args[0];
        String user = args[1];
        String password = args[2];
        String url = args[3];
        String topic = args[4];
        String medicinesListPath = args[5];

        BlueskyController control = createBlueskyEnvironment(url, topic, refreshToken, user, password);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, control, medicinesListPath);
    }

    private static void autoExecute(ScheduledExecutorService scheduler,
                                    BlueskyController control,
                                    String medicinesListPath) {
        log.info("Bluesky is running...");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute(medicinesListPath);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static BlueskyController createBlueskyEnvironment(String url, String topic, String refreshToken, String user, String password) throws IOException, InterruptedException {
        BlueskyGetToken getToken = new BlueskyGetToken(refreshToken, user, password);

        ActiveMQBlueskyStore store = new ActiveMQBlueskyStore(url, topic);
        BlueskyFeeder feeder = new BlueskyFeeder(getToken);

        return new BlueskyController(feeder, store);
    }
}