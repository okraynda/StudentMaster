package com.studentmanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE - Menambahkan data mahasiswa
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students " +
                "(nim, nama, jenis_kelamin, jurusan, email, no_telepon, foto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getNim());
            stmt.setString(2, student.getNama());
            stmt.setString(3, student.getJenisKelamin());
            stmt.setString(4, student.getJurusan());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getNoTelepon());
            stmt.setString(7, student.getFoto());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal menambahkan mahasiswa: " + e.getMessage());
            return false;
        }
    }

    // READ - Menampilkan seluruh data mahasiswa
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("jurusan"),
                        rs.getString("email"),
                        rs.getString("no_telepon"),
                        rs.getString("foto")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
        }

        return students;
    }

    // UPDATE - Memperbarui data mahasiswa
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET nim=?, nama=?, jenis_kelamin=?, " +
                "jurusan=?, email=?, no_telepon=?, foto=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getNim());
            stmt.setString(2, student.getNama());
            stmt.setString(3, student.getJenisKelamin());
            stmt.setString(4, student.getJurusan());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getNoTelepon());
            stmt.setString(7, student.getFoto());
            stmt.setInt(8, student.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal memperbarui data: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Menghapus data mahasiswa
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal menghapus data: " + e.getMessage());
            return false;
        }
    }
}