package viper.sentinox;

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

    private static void autoExecute(ScheduledExecutorService scheduler, PubChemControl pubChemControl) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pubChemControl.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static PubChemControl createPubChemEnvironment() {
        PubChemConnect connect = new PubChemConnect();
        PubChemGet get = new PubChemGet(connect);
        PubChemInsert insert = new PubChemInsert(connect, get);
        PubChemFeeder feeder = new PubChemFeeder(insert, connect);

        return new PubChemControl(feeder);
    }
}