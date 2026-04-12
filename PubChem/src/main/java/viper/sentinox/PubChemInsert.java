package viper.sentinox;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PubChemInsert {

    private final PubChemConnect pubChemConnect;
    private final PubChemGet pubChemGet;

    public PubChemInsert(PubChemConnect pubChemConnect, PubChemGet pubChemGet) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemGet = pubChemGet;
    }

    public void saveReactions(String databaseURL) throws IOException {
        ArrayList<String> reactions = pubChemGet.getReactions();

        if (reactions.isEmpty()) {
            System.out.println("No reactions to insert");
            return;
        }

        String medicine = pubChemConnect.getMedicine();
        String cid = pubChemConnect.getCID();

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            insertMedicine(conn, cid, medicine);
            int medicineId = getMedicineDatabaseId(conn, cid);

            if (medicineId == -1) {
                System.out.println("Medicine not found in database");
                return;
            }

            insertReactions(conn, medicineId, reactions);

        } catch (SQLException e) {
            System.out.println("Error connecting to the PubChem database");
        }
    }

    public void saveMedicine(String databaseURL) throws IOException {
        String medicine = pubChemConnect.getMedicine();
        String cid = pubChemConnect.getCID();

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            insertMedicine(conn, cid, medicine);
            System.out.println(medicine + " added correctly");
        } catch (SQLException e) {
            System.out.println("Error inserting " + medicine);
        }
    }

    public void saveMedicinesFromList(String[] medicines, String databaseURL) throws IOException {
        for (String medicine : medicines) {
            pubChemConnect.setMedicine(medicine);
            saveMedicine(databaseURL);
        }
    }

    private void insertMedicine(Connection conn, String cid, String medicine) throws SQLException {
        String sql = """
            INSERT OR IGNORE INTO medicines (cid, name)
            VALUES (?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);
            stmt.setString(2, medicine);
            stmt.executeUpdate();
        }
    }

    private int getMedicineDatabaseId(Connection conn, String cid) throws SQLException {
        String sql = """
            SELECT id FROM medicines
            WHERE cid = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return -1;
    }

    private void insertReactions(Connection conn, int medicineId, ArrayList<String> reactions) throws SQLException {
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
}