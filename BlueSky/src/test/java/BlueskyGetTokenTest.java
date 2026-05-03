import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.control.BlueskyGetToken;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlueskyGetTokenTest {

    private static final Path TOKEN_FILE = Path.of("BlueskyToken.txt");

    @AfterEach
    void cleanFile() throws IOException {
        Files.deleteIfExists(TOKEN_FILE);
    }

    @Test
    void getAccessToken_returnsAccessToken_andStoresRefreshToken() throws IOException, InterruptedException {
        String responseBody = """
                {
                  "accessJwt": "new-access-token",
                  "refreshJwt": "new-refresh-token"
                }
                """;

        HttpClient client = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(responseBody);
        when(client.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        BlueskyGetToken blueskyGetToken = new BlueskyGetToken("old-refresh-token", "user123", "password123");

        String token = blueskyGetToken.getAccessToken();

        assertEquals("new-access-token", token);
        assertEquals("new-refresh-token", Files.readString(TOKEN_FILE));
    }
}