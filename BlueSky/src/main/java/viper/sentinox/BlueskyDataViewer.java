package viper.sentinox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlueskyDataViewer {

    public void showPosts(String databaseURL) {
        String sql = "SELECT * FROM bluesky_posts ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("----- BLUESKY POSTS -----");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Medicine: " + rs.getString("medicine"));
                System.out.println("Author: " + rs.getString("author_handle"));
                System.out.println("Text: " + rs.getString("post_text"));
                System.out.println("Post URI: " + rs.getString("post_uri"));
                System.out.println("Created at: " + rs.getString("created_at"));
                System.out.println("Captured at: " + rs.getString("captured_at"));
                System.out.println("-------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the Bluesky database");
        }
    }
}