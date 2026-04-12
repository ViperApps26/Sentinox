package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class PubChemConnect {

    private final String baseUrl;
    private final Gson gson;

    private String medicine;

    public PubChemConnect() {
        this.baseUrl = "https://pubchem.ncbi.nlm.nih.gov/rest";
        this.gson = new Gson();
        this.medicine = "ibuprofen";
    }

    public PubChemConnect(String medicine) {
        this();
        this.medicine = medicine;
    }

    public String getCID() throws IOException {
        String path = String.format("/compound/name/%s/cids/JSON", medicine);

        Connection.Response response = cidRequest(path);
        String body = response.body();

        JsonObject json = gson.fromJson(body, JsonObject.class);

        return json.getAsJsonObject("IdentifierList")
                .getAsJsonArray("CID")
                .get(0)
                .getAsString();
    }

    public JsonObject connect() throws IOException {
        String cid = getCID();
        String path = "/pug_view/data/compound/" + cid + "/JSON";

        Connection.Response response = request(path);
        String body = response.body();

        return gson.fromJson(body, JsonObject.class);
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

    public String getMedicine() {
        return medicine;
    }
}//test//test