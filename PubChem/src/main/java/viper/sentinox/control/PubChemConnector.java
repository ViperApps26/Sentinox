package viper.sentinox.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class PubChemConnector {

    private final String baseUrl;
    private String medicine;

    public PubChemConnector() {
        this.baseUrl = "https://pubchem.ncbi.nlm.nih.gov/rest";
        this.medicine = "ibuprofen";
    }

    public JsonObject connector() throws IOException {
        String cid = getCID();
        String path = "/pug_view/data/compound/" + cid + "/JSON";

        Connection.Response response = request(path);
        String body = response.body();

        return parseOrNull(body);
    }

    private JsonObject parseOrNull(String body) {
        try {
            return new Gson().fromJson(body, JsonObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    private Connection.Response cidRequest(String path) throws IOException {
        return Jsoup.connect(baseUrl + "/pug" + path)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    private Connection.Response request(String path) throws IOException {
        return Jsoup.connect(baseUrl + path)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getCID() throws IOException {
        String path = String.format("/compound/name/%s/cids/JSON", medicine);

        Connection.Response response = cidRequest(path);
        String body = response.body();

        JsonObject json = new Gson().fromJson(body, JsonObject.class);

        return json.getAsJsonObject("IdentifierList")
                .getAsJsonArray("CID")
                .get(0)
                .getAsString();
    }

    public String getMedicine() {
        return medicine;
    }
}