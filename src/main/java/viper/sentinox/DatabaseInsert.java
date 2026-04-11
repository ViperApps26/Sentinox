package viper.sentinox;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInsert {
    public static void saveReactions(String databaseURL) throws IOException {
        ArrayList<String> reactions = PubChemGet.getReactions();

        if (!reactions.isEmpty()) {
            String medicine = PubChemConnect.medicine;
            String cid = PubChemConnect.getCID();

            try (Connection conn = DriverManager.getConnection(databaseURL)) {
                insertMedicine(conn, cid, medicine);
                int medicineId = getMedicineDatabaseId(conn, cid);
                insertReactions(conn, medicineId, reactions);
            } catch (SQLException e) {
                System.out.println("Error connecting to the database");
            }
        } else {
            System.out.println("No reactions to insert");
        }

    }

    private static void insertReactions(Connection conn, int medicineId, ArrayList<String> reactions) throws SQLException {
        String sql = """
                INSERT INTO pubchem_reactions (medicine_id, reaction)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String reaction : reactions) {
                stmt.setInt(1, medicineId);
                stmt.setString(2, reaction);
                stmt.executeUpdate();
            }
        }
    }

    public static void saveMedicinesFromList(String[] medicines, String databaseURL) throws IOException {
        for (String medicine : medicines) {
            PubChemConnect.setMedicine(medicine);
            String cid = PubChemConnect.getCID();

            try (Connection conn = DriverManager.getConnection(databaseURL)) {
                insertMedicine(conn, cid, medicine);

                System.out.println(medicine + " added correctly");

            } catch (SQLException e) {
                System.out.println("Error inserting " + medicine);
            }
        }
    }

    private static void insertMedicine(Connection conn, String cid, String medicine) throws SQLException {

        String insertSql = """
                INSERT OR IGNORE INTO medicines (cid, name)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, cid);
            stmt.setString(2, medicine);
            stmt.executeUpdate();
        }
    }

    private static int getMedicineDatabaseId(Connection conn, String cid) throws SQLException {
        String selectSql = """
                SELECT id FROM medicines
                WHERE cid = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, cid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    public static void saveBlueskyPosts(String token, String databaseURL) throws IOException {
        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            String medicine = BlueskyConnect.query;
            PubChemConnect.setMedicine(medicine);
            String cid = PubChemConnect.getCID();
            insertMedicine(conn, cid, medicine);

            int medicineId = getMedicineDatabaseId(conn, PubChemConnect.getCID());
            if (medicineId == -1) {
                System.out.println("No Results Found");
            } else {
                JsonArray postAttributes = BlueskyGet.getPostsAttributes(token);

                List<String> authors = BlueskyGet.getAuthors(postAttributes);
                List<String> posts = BlueskyGet.getPosts(postAttributes);
                List<String> uris = BlueskyGet.getUris(postAttributes);
                List<String> creationDates = BlueskyGet.getCreationDate(postAttributes);

                insertBlueskyPosts(conn, medicineId, medicine, authors, posts, uris, creationDates);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }

    private static void insertBlueskyPosts(Connection conn,
                                           int medicineId,
                                           String medicine,
                                           List<String> authors,
                                           List<String> posts,
                                           List<String> uris,
                                           List<String> creationDates) throws SQLException {

        String sql = """
            INSERT INTO bluesky_posts (medicine_id, medicine, author_handle, post_text, post_uri, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < posts.size(); i++) {
                stmt.setInt(1, medicineId);
                stmt.setString(2, medicine);
                stmt.setString(3, authors.get(i));
                stmt.setString(4, posts.get(i));
                stmt.setString(5, uris.get(i));
                stmt.setString(6, creationDates.get(i));
                stmt.executeUpdate();
            }
        }
    }

    public static void updateMedicinesData(String token, String databaseURL) throws IOException {
        List<String> medicines = new ArrayList<>();
        getMedicinesList(medicines, databaseURL);
        for (String medicine : medicines) {
            PubChemConnect.setMedicine(medicine);
            saveReactions(databaseURL);

            BlueskyConnect.setQuery(medicine);
            saveBlueskyPosts(token, databaseURL);

            System.out.println(medicine + " data update correctly");
        }
    }

    public static void getMedicinesList(List<String> medicines, String databaseURL) {
        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            String sql = "SELECT id, name FROM medicines";

            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicines.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
        }
    }
}

