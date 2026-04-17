package viper.sentinox;

import java.io.IOException;

public class PubChemFeeder {

    private final PubChemInsert pubChemInsert;
    private final PubChemConnect pubChemConnect;

    public PubChemFeeder(PubChemInsert pubChemInsert, PubChemConnect pubChemConnect) {
        this.pubChemInsert = pubChemInsert;
        this.pubChemConnect = pubChemConnect;
    }

    public void feedReactionsFromList(String[] medicines) throws IOException {
        for (String medicine : medicines) {
            pubChemConnect.setMedicine(medicine);
            pubChemInsert.publishReactions();
        }
    }
}