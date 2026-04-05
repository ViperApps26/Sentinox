package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {

    public static void savePubChemMedicineAndReactions() throws IOException {
        String medicine = PubChemConnect.medicine;
        String cid = PubChemConnect.getCID();
        ArrayList<String> reactions = PubChemGet.getReactions();

        try (Connection conn = DatabaseManager.connect()) {
            int medicineId = insertMedicineIfNotExists(conn, cid, medicine);
            insertReactions(conn, medicineId, reactions);
            System.out.println("Medicine and reactions saved correctly.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveInitialMedicines(String token) {
        String[] medicines = {
                "ibuprofen",
                "paracetamol",
                "aspirin",
                "amoxicillin"
        };

        saveMedicinesFromList(medicines, token);
    }

    public static void saveMedicinesFromList(String[] medicines, String token) {
        for (String medicine : medicines) {
            try {
                String cleanMedicine = medicine.trim();


                PubChemConnect.setMedicine(cleanMedicine);
                savePubChemMedicineAndReactions();


                BlueskyConnect.setQuery(cleanMedicine);

                if (token != null && !token.isBlank()) {
                    String blueskyToken = BlueskyGetToken.getToken(token);
                    saveBlueskyPosts(blueskyToken);
                }

                System.out.println("FULL DATA saved for: " + cleanMedicine);

            } catch (Exception e) {
                System.out.println("Error with " + medicine + ": " + e.getMessage());
            }
        }
    }



    public static void saveMedicinesFromFile(String token) {
        try {
            List<String> medicines = Files.readAllLines(Path.of("medicines.txt"));
            saveMedicinesFromList(medicines.toArray(new String[0]), token);
            System.out.println("Medicines from file saved correctly.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int insertMedicineIfNotExists(Connection conn, String cid, String medicine) throws SQLException {
        String insertSql = """
            INSERT OR IGNORE INTO medicines (cid, name)
            VALUES (?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, cid);
            stmt.setString(2, medicine);
            stmt.executeUpdate();
        }

        String selectSql = """
            SELECT id FROM medicines
            WHERE cid = ? OR name = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, cid);
            stmt.setString(2, medicine);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        throw new SQLException("Could not retrieve medicine id.");
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

    public static void saveBlueskyPosts(String token) throws IOException {
        JsonObject response = BlueskyConnect.connect(token);
        JsonArray posts = response.getAsJsonArray("posts");
        String medicine = BlueskyConnect.query;

        try (Connection conn = DatabaseManager.connect()) {
            Integer medicineId = getMedicineIdByName(conn, medicine);

            String sql = """
                INSERT INTO bluesky_posts (medicine_id, medicine, author_handle, post_text, post_uri, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < posts.size(); i++) {
                    JsonObject post = posts.get(i).getAsJsonObject();

                    if (medicineId != null) {
                        stmt.setInt(1, medicineId);
                    } else {
                        stmt.setNull(1, Types.INTEGER);
                    }

                    stmt.setString(2, medicine);
                    stmt.setString(3, extractAuthorHandle(post));
                    stmt.setString(4, extractPostText(post));
                    stmt.setString(5, extractPostUri(post));
                    stmt.setString(6, extractCreatedAt(post));

                    stmt.executeUpdate();
                }
            }

            System.out.println("Bluesky posts saved correctly.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Integer getMedicineIdByName(Connection conn, String medicine) throws SQLException {
        String sql = "SELECT id FROM medicines WHERE name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medicine);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }

    private static String extractAuthorHandle(JsonObject post) {
        if (post.has("author") && post.getAsJsonObject("author").has("handle")) {
            return post.getAsJsonObject("author").get("handle").getAsString();
        }
        return null;
    }

    private static String extractPostText(JsonObject post) {
        if (post.has("record") && post.getAsJsonObject("record").has("text")) {
            return post.getAsJsonObject("record").get("text").getAsString();
        }
        return "";
    }

    private static String extractPostUri(JsonObject post) {
        if (post.has("uri")) {
            return post.get("uri").getAsString();
        }
        return null;
    }

    private static String extractCreatedAt(JsonObject post) {
        if (post.has("record") && post.getAsJsonObject("record").has("createdAt")) {
            return post.getAsJsonObject("record").get("createdAt").getAsString();
        }
        return null;
    }
}

