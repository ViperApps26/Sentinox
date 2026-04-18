package viper.sentinox;

import viper.sentinox.control.PubChemControl;
import viper.sentinox.control.PubChemFeeder;
import viper.sentinox.control.PubChemPublisher;
import viper.sentinox.model.PubChemConnect;
import viper.sentinox.model.PubChemGet;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        PubChemControl pubChemControl = createPubChemEnvironment();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, pubChemControl);
    }

    private static void autoExecute(ScheduledExecutorService scheduler, PubChemControl control) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static PubChemControl createPubChemEnvironment() {
        PubChemConnect connect = new PubChemConnect();
        PubChemGet get = new PubChemGet(connect);
        PubChemPublisher publisher = new PubChemPublisher(connect, get);
        PubChemFeeder feeder = new PubChemFeeder(publisher, connect);

        return new PubChemControl(feeder);
    }
}