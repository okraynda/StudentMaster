package com.studentmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_management";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            System.out.println("Koneksi database berhasil!");
            return connection;

        } catch (SQLException e) {
            System.out.println("Koneksi database gagal!");
            e.printStackTrace();
            return null;
        }
    }
}