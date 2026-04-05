package viper.sentinox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:sentinox.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createMedicinesTable = """
            CREATE TABLE IF NOT EXISTS medicines (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cid TEXT UNIQUE,
                name TEXT NOT NULL UNIQUE,
                captured_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """;

        String createReactionsTable = """
            CREATE TABLE IF NOT EXISTS pubchem_reactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                medicine_id INTEGER NOT NULL,
                reaction TEXT NOT NULL,
                captured_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (medicine_id) REFERENCES medicines(id)
            );
            """;

        String createBlueskyTable = """
            CREATE TABLE IF NOT EXISTS bluesky_posts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                medicine_id INTEGER,
                medicine TEXT NOT NULL,
                author_handle TEXT,
                post_text TEXT NOT NULL,
                post_uri TEXT,
                created_at TEXT,
                captured_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (medicine_id) REFERENCES medicines(id)
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createMedicinesTable);
            stmt.execute(createReactionsTable);
            stmt.execute(createBlueskyTable);

            System.out.println("Database initialised correctly.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
