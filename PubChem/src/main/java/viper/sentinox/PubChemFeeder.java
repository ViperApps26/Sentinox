package viper.sentinox;

import java.io.IOException;

public class PubChemFeeder {

    private final PubChemPublisher pubChemPublisher;
    private final PubChemConnect pubChemConnect;

    public PubChemFeeder(PubChemPublisher pubChemPublisher, PubChemConnect pubChemConnect) {
        this.pubChemPublisher = pubChemPublisher;
        this.pubChemConnect = pubChemConnect;
    }

    public void feedReactionsFromList(String[] medicines) throws IOException {
        for (String medicine : medicines) {
            pubChemConnect.setMedicine(medicine);
            pubChemPublisher.publishReactions();
        }
    }
}