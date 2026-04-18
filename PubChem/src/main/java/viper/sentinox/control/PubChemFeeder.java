package viper.sentinox.control;

import viper.sentinox.model.PubChemConnect;

import java.io.IOException;

public class PubChemFeeder implements PubChemFeederInterface {

    private final PubChemPublisher publisher;
    private final PubChemConnect connect;

    public PubChemFeeder(PubChemPublisher publisher, PubChemConnect connect) {
        this.publisher = publisher;
        this.connect = connect;
    }

    public void feedReactionsFromList(String[] medicines) throws IOException {
        for (String medicine : medicines) {
            connect.setMedicine(medicine);
            publisher.publishReactions();
        }
    }
}