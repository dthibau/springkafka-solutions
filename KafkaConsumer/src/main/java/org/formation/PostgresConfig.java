package org.formation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/coursier";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";


    public static void insererOffset(String idCouriser, long offset) {
        String sql = "INSERT INTO coursier (coursierId, kafkaOffset) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idCouriser);
            stmt.setLong(2, offset);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
