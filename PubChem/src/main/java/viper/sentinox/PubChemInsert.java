package viper.sentinox;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class PubChemInsert {

    private final PubChemConnect pubChemConnect;
    private final PubChemGet pubChemGet;
    private final PubChemPublisher pubChemPublisher;
    private final Gson gson;

    public PubChemInsert(PubChemConnect pubChemConnect, PubChemGet pubChemGet) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemGet = pubChemGet;
        this.pubChemPublisher = new PubChemPublisher();
        this.gson = new Gson();
    }

    public void publishReactions() throws IOException {
        ArrayList<String> reactions = pubChemGet.getReactions();

        String medicine = pubChemConnect.getMedicine();
        String cid = pubChemConnect.getCID();

        if (reactions.isEmpty()) {
            System.out.println(medicine + " has no reactions to publish");
            return;
        }

        for (String reaction : reactions) {
            PubChemEvent event = new PubChemEvent(
                    System.currentTimeMillis(),
                    "PubChemFeeder",
                    medicine,
                    cid,
                    reaction
            );

            String json = gson.toJson(event);
            pubChemPublisher.publish(json);
        }

        System.out.println(medicine + " reactions published correctly");
    }
}