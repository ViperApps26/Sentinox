package viper.sentinox;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BlueskyInsert {

    private final BlueskyConnect blueskyConnect;
    private final BlueskyGet blueskyGet;

    public BlueskyInsert(BlueskyConnect blueskyConnect, BlueskyGet blueskyGet) {
        this.blueskyConnect = blueskyConnect;
        this.blueskyGet = blueskyGet;
    }

    public void getMedicinesList(List<String> medicines, String databaseURL) {
        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT name FROM medicines";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    medicines.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }

    public void savePosts(String token, String databaseURL) throws IOException {
        JsonArray postAttributes = blueskyGet.getPostsAttributes(token);

        List<String> authors = blueskyGet.getAuthors(postAttributes);
        List<String> posts = blueskyGet.getPosts(postAttributes);
        List<String> creationDates = blueskyGet.getCreationDates(postAttributes);

        if (posts.isEmpty()) {
            System.out.println(blueskyConnect.getQuery() + " has no posts to insert");
            return;
        }

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            insertBlueskyPosts(
                    conn,
                    blueskyConnect.getQuery(),
                    authors,
                    posts,
                    creationDates
            );
            System.out.println(blueskyConnect.getQuery() + " posts added correctly");
        } catch (SQLException e) {
            System.out.println("Error connecting to the Bluesky database");
        }
    }

    private void insertBlueskyPosts(Connection conn,
                                    String medicine,
                                    List<String> authors,
                                    List<String> posts,
                                    List<String> creationDates) throws SQLException {

        String sql = """
            INSERT INTO bluesky_posts (medicine, author_handle, post_text, created_at)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < posts.size(); i++) {
                stmt.setString(1, medicine);
                stmt.setString(2, authors.get(i));
                stmt.setString(3, posts.get(i));
                stmt.setString(4, creationDates.get(i));
                stmt.executeUpdate();
            }
        }
    }
}