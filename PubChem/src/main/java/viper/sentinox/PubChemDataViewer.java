package viper.sentinox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PubChemDataViewer {

    public void showMedicines(String databaseURL) {
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
            System.out.println("Error connecting to the PubChem database");
        }
    }

    public void showReactions(String databaseURL) {
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
                System.out.println("-----------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the PubChem database");
        }
    }

    public void showSummary(String databaseURL) {
        String sql = """
            SELECT
                m.id,
                m.cid,
                m.name,
                m.captured_at,
                COALESCE(GROUP_CONCAT(DISTINCT r.reaction), '') AS reactions
            FROM medicines m
            LEFT JOIN pubchem_reactions r ON m.id = r.medicine_id
            GROUP BY m.id, m.cid, m.name, m.captured_at
            ORDER BY m.id DESC
            """;

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("----- PUBCHEM SUMMARY -----");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("CID: " + rs.getString("cid"));
                System.out.println("Medicine: " + rs.getString("name"));
                System.out.println("Reactions: " + rs.getString("reactions"));
                System.out.println("Captured at: " + rs.getString("captured_at"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the PubChem database");
        }
    }
}