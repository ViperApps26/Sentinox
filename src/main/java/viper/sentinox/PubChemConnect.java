package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class PubChemConnect {

    private static final String BASE_URL = "https://pubchem.ncbi.nlm.nih.gov/rest";

    public static String medicine = "ibuprofen";

    public static String getCID() throws IOException {
        String path = String.format("/compound/name/%s/cids/JSON", medicine);

        Connection.Response response = cidRequest(path);
        String body = response.body();

        JsonObject json = new Gson().fromJson(body, JsonObject.class);

        return json.getAsJsonObject("IdentifierList")
                .getAsJsonArray("CID")
                .get(0).getAsString();
    }

    public static JsonObject connect() throws IOException {
        String cid = getCID();

        String path = "/pug_view/data/compound/" + cid + "/JSON";

        Connection.Response response = request(path);

        String body = response.body();

        return body.isEmpty()
                ? new Gson().fromJson(body, JsonObject.class)
                : null;
    }

    private static Connection.Response cidRequest(String path) throws IOException {
        return Jsoup.connect(BASE_URL + "/pug" + path)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    private static Connection.Response request(String path) throws IOException {
        return Jsoup.connect(BASE_URL + path)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    public static void setMedicine(String medicine) {
        PubChemConnect.medicine = medicine;
    }
}