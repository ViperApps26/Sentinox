package viper.sentinox;

import java.sql.*;

public class DataViewer {

    public static void showMedicineSummary(String databaseURL) {
        String sql = """
            SELECT
                m.id,
                m.cid,
                m.name,
                m.captured_at,
                COALESCE(GROUP_CONCAT(DISTINCT r.reaction), '') AS reactions,
                COALESCE(GROUP_CONCAT(DISTINCT b.post_text), '') AS comments
            FROM medicines m
            LEFT JOIN pubchem_reactions r ON m.id = r.medicine_id
            LEFT JOIN bluesky_posts b ON m.id = b.medicine_id
            GROUP BY m.id, m.cid, m.name, m.captured_at
            ORDER BY m.id DESC
            """;

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("----- MEDICINE SUMMARY -----");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("CID: " + rs.getString("cid"));
                System.out.println("Medicine: " + rs.getString("name"));
                System.out.println("Reactions: " + rs.getString("reactions"));
                System.out.println("Comments: " + rs.getString("comments"));
                System.out.println("Captured at: " + rs.getString("captured_at"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }

    public static void showMedicines(String databaseURL) {
        String sql = "SELECT * FROM medicines ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("----- MEDICINES -----");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("CID: " + rs.getString("cid"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Captured at: " + rs.getString("captured_at"));
                System.out.println("---------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }

    public static void showPubChemReactions(String databaseURL) {
        String sql = """
            SELECT r.id, m.name AS medicine, m.cid, r.reaction, r.captured_at
            FROM pubchem_reactions r
            JOIN medicines m ON r.medicine_id = m.id
            ORDER BY r.id DESC
            """;

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("----- PUBCHEM REACTIONS -----");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("CID: " + rs.getString("cid"));
                System.out.println("Medicine: " + rs.getString("medicine"));
                System.out.println("Reaction: " + rs.getString("reaction"));
                System.out.println("Captured at: " + rs.getString("captured_at"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }

    public static void showBlueskyPosts(String databaseURL) {
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
            System.out.println("Error connecting to the database");
        }
    }
}



