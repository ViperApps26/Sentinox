package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class OpenFDAConnect {
    private static final String BASE_URL = "https://api.fda.gov";

    public static String medicine = "nolotil";
    public static int limit = 1;
    public static String startDate = "20250101";
    public static String finalDate = "20250201";

    public static JsonObject connect(String apiKey) throws IOException {
        String path = getOpenFdaPath(apiKey);

        Connection.Response response = request(path);
        String body = response.body();

        return new Gson().fromJson(body, JsonObject.class);
    }

    private static String getOpenFdaPath(String apiKey) {
        String pattern = "/drug/event.json?api_key=%s&search=receivedate:[%s+TO+%s]+AND+patient.drug.medicinalproduct:%s&limit=%s";

        return String.format(pattern,
                apiKey,
                startDate,
                finalDate,
                medicine,
                limit
        );
    }

    private static Connection.Response request(String url) throws IOException {
        return Jsoup.connect(BASE_URL + url)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .method(Connection.Method.GET)
                .execute();
    }

    public static void setMedicine(String medicine) {
        OpenFDAConnect.medicine = medicine;
    }

    public static void setLimit(int limit) {
        OpenFDAConnect.limit = limit;
    }

    public static void setStartDate(String startDate) {
        OpenFDAConnect.startDate = startDate;
    }

    public static void setFinalDate(String finalDate) {
        OpenFDAConnect.finalDate = finalDate;
    }
}
