package viper.sentinox.control;

import com.google.gson.JsonObject;
import java.io.IOException;

public interface BlueskyConnectorInterface {
    JsonObject connector(String token) throws IOException;

    void setQuery(String query);
    void setLimit(int limit);
    void setStartDate(String newDate);
    void setFinalDate(String newDate);

    String getQuery();
    int getLimit();
    String getStartDate();
    String getFinalDate();
}
