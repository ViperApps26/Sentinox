import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import viper.sentinox.BlueskyGetToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class BlueskyGetTokenTest {

    private static final Path TOKEN_FILE = Path.of("BlueskyToken.txt");

    @AfterEach
    void cleanFile() throws IOException {
        Files.deleteIfExists(TOKEN_FILE);
    }

    @Test
    void getToken_returnsAccessToken() throws IOException {
        String responseBody = """
                {
                  "accessJwt": "new-access-token",
                  "refreshJwt": "new-refresh-token"
                }
                """;

        Connection connection = mock(Connection.class);
        Connection.Response response = mock(Connection.Response.class);

        when(connection.ignoreContentType(true)).thenReturn(connection);
        when(connection.header(anyString(), anyString())).thenReturn(connection);
        when(connection.method(Connection.Method.POST)).thenReturn(connection);
        when(connection.execute()).thenReturn(response);
        when(response.body()).thenReturn(responseBody);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            jsoupMock.when(() ->
                            Jsoup.connect("https://bsky.social/xrpc/com.atproto.server.refreshSession"))
                    .thenReturn(connection);

            BlueskyGetToken blueskyGetToken = new BlueskyGetToken();

            String token = blueskyGetToken.getToken("old-refresh-token");

            assertEquals("new-access-token", token);
        }
    }
}//test