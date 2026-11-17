package dao;

import model.Tamu;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Tamu.
 * Menangani operasi CRUD database lengkap untuk Tamu.
 */
public class TamuDAO {

    public void tambahTamu(Tamu tamu) throws SQLException {
        String sql = "INSERT INTO tamu (nama, nomor_identitas, no_telepon, email, alamat) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tamu.getNama());
            stmt.setString(2, tamu.getNomorIdentitas());
            stmt.setString(3, tamu.getNoTelepon());
            stmt.setString(4, tamu.getEmail());
            stmt.setString(5, tamu.getAlamat());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tamu.setIdTamu(rs.getInt(1));
                }
            }
        }
    }

    public List<Tamu> getAllTamu() throws SQLException {
        List<Tamu> list = new ArrayList<>();
        String sql = "SELECT * FROM tamu ORDER BY id_tamu DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToTamu(rs));
            }
        }
        return list;
    }

    public Tamu getTamuById(int id) throws SQLException {
        String sql = "SELECT * FROM tamu WHERE id_tamu = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTamu(rs);
                }
            }
        }
        return null;
    }

    public void updateTamu(Tamu tamu) throws SQLException {
        String sql = "UPDATE tamu SET nama = ?, nomor_identitas = ?, no_telepon = ?, email = ?, alamat = ? WHERE id_tamu = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tamu.getNama());
            stmt.setString(2, tamu.getNomorIdentitas());
            stmt.setString(3, tamu.getNoTelepon());
            stmt.setString(4, tamu.getEmail());
            stmt.setString(5, tamu.getAlamat());
            stmt.setInt(6, tamu.getIdTamu());
            stmt.executeUpdate();
        }
    }

    public void deleteTamu(int id) throws SQLException {
        String sql = "DELETE FROM tamu WHERE id_tamu = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Tamu> searchTamu(String keyword) throws SQLException {
        List<Tamu> list = new ArrayList<>();
        String sql = "SELECT * FROM tamu WHERE nama LIKE ? OR nomor_identitas LIKE ? OR no_telepon LIKE ? OR email LIKE ? ORDER BY id_tamu DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTamu(rs));
                }
            }
        }
        return list;
    }

    public int getTotalTamu() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tamu";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Tamu mapResultSetToTamu(ResultSet rs) throws SQLException {
        return new Tamu(
            rs.getInt("id_tamu"),
            rs.getString("nama"),
            rs.getString("nomor_identitas"),
            rs.getString("no_telepon"),
            rs.getString("email"),
            rs.getString("alamat")
        );
    }
}
