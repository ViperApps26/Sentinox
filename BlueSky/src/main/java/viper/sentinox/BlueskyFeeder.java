package viper.sentinox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlueskyFeeder {

    private final BlueskyGetToken blueskyGetToken;
    private final BlueskyInsert blueskyInsert;
    private final BlueskyConnect blueskyConnect;

    public BlueskyFeeder(BlueskyGetToken blueskyGetToken,
                         BlueskyInsert blueskyInsert,
                         BlueskyConnect blueskyConnect) {
        this.blueskyGetToken = blueskyGetToken;
        this.blueskyInsert = blueskyInsert;
        this.blueskyConnect = blueskyConnect;
    }

    public void feedMedicines(String token, String password, String databaseURL)
            throws IOException, InterruptedException {

        List<String> medicines = new ArrayList<>();
        blueskyInsert.getMedicinesList(medicines, databaseURL);

        if (medicines.isEmpty()) {
            System.out.println("No medicines found in database");
            return;
        }

        String accessToken = blueskyGetToken.getAccessToken(token, password);

        for (String medicine : medicines) {
            blueskyConnect.setQuery(medicine);
            blueskyInsert.savePosts(accessToken, databaseURL);
        }
    }
}