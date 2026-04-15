package viper.sentinox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BlueskyDatabaseCreator {

    public void createDatabase(String databaseURL) {
        String createBlueskyTable = """
            CREATE TABLE IF NOT EXISTS bluesky_posts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                medicine TEXT NOT NULL,
                author_handle TEXT,
                post_text TEXT NOT NULL,
                created_at TEXT,
                captured_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """;

        try (Connection conn = DriverManager.getConnection(databaseURL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createBlueskyTable);

        } catch (SQLException e) {
            System.out.println("Error connecting to the Bluesky database");
        }
    }
}