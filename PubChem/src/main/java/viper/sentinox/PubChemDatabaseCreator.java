package viper.sentinox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PubChemDatabaseCreator {

    public void createDatabase(String databaseURL) {
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

        try (Connection conn = DriverManager.getConnection(databaseURL);
            Statement stmt = conn.createStatement()) {

            stmt.execute(createMedicinesTable);
            stmt.execute(createReactionsTable);

        } catch (SQLException e) {
            System.out.println("Error connecting to the PubChem database");
        }
    }
}