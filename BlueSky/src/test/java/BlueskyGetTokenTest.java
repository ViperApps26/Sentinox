import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.BlueskyGetToken;

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

        BlueskyGetToken blueskyGetToken = new BlueskyGetToken(
                "https://bsky.social/xrpc/com.atproto.server.createSession",
                "https://bsky.social/xrpc/com.atproto.server.refreshSession",
                "vicraft.bsky.social",
                TOKEN_FILE,
                client
        );

        String token = blueskyGetToken.getAccessToken("old-refresh-token", "password123");

        assertEquals("new-access-token", token);
        assertEquals("new-refresh-token", Files.readString(TOKEN_FILE));
    }
}
