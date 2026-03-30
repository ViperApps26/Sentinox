package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class OpenFDAConnect {
    private static final String BASE_URL = "https://dailymed.nlm.nih.gov/dailymed/services/v2";

    public static String medicine = "Ibuprofen";

    public static JsonObject connect() throws IOException {
        String path = getDailyMedPath();

        Connection.Response response = request(path);
        String body = response.body();

        return new Gson().fromJson(body, JsonObject.class);
    }

    private static String getDailyMedPath() {
        String pattern = "/spls.json?drug_name=%s";

        return String.format(pattern,
                medicine
        );
    }

    private static Connection.Response request(String path) throws IOException {
        return Jsoup.connect(BASE_URL + path)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    public static JsonObject connectId() throws IOException {
        String path = getDailyMedPathId(OpenFDAGet.getIds());

        Connection.Response response = requestId(path);
        String xmlBody = response.body();
        String body = org.json.XML.toJSONObject(xmlBody).toString();

        return new Gson().fromJson(body, JsonObject.class);
    }

    private static String getDailyMedPathId(ArrayList<String> ids) {
        return "/spls/" + ids.getFirst() + ".xml";
    }

    private static Connection.Response requestId(String path) throws IOException {
        return Jsoup.connect(BASE_URL + path)
                .ignoreContentType(true)
                .execute();
    }

    public static void setMedicine(String medicine) {
        OpenFDAConnect.medicine = medicine;
    }
}
