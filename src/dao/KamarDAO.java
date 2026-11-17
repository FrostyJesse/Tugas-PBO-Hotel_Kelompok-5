package dao;

import model.Deluxe;
import model.Kamar;
import model.Standard;
import model.Suite;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Kamar.
 * Menangani operasi CRUD database lengkap untuk Kamar dengan instansiasi Polimorfik.
 */
public class KamarDAO {

    public void tambahKamar(Kamar kamar) throws SQLException {
        String sql = "INSERT INTO kamar (nomor_kamar, tipe_kamar, harga, status, kapasitas) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kamar.getNomorKamar());
            stmt.setString(2, kamar.getTipeKamar());
            stmt.setDouble(3, kamar.getHarga());
            stmt.setString(4, kamar.getStatus());
            stmt.setInt(5, kamar.getKapasitas());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    kamar.setIdKamar(rs.getInt(1));
                }
            }
        }
    }

    public List<Kamar> getAllKamar() throws SQLException {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar ORDER BY nomor_kamar ASC";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToKamar(rs));
            }
        }
        return list;
    }

    public Kamar getKamarById(int id) throws SQLException {
        String sql = "SELECT * FROM kamar WHERE id_kamar = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKamar(rs);
                }
            }
        }
        return null;
    }

    public Kamar getKamarByNomor(String nomorKamar) throws SQLException {
        String sql = "SELECT * FROM kamar WHERE nomor_kamar = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomorKamar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKamar(rs);
                }
            }
        }
        return null;
    }

    public void updateKamar(Kamar kamar) throws SQLException {
        String sql = "UPDATE kamar SET nomor_kamar = ?, tipe_kamar = ?, harga = ?, status = ?, kapasitas = ? WHERE id_kamar = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kamar.getNomorKamar());
            stmt.setString(2, kamar.getTipeKamar());
            stmt.setDouble(3, kamar.getHarga());
            stmt.setString(4, kamar.getStatus());
            stmt.setInt(5, kamar.getKapasitas());
            stmt.setInt(6, kamar.getIdKamar());
            stmt.executeUpdate();
        }
    }

    public void updateStatusKamar(int idKamar, String status) throws SQLException {
        String sql = "UPDATE kamar SET status = ? WHERE id_kamar = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idKamar);
            stmt.executeUpdate();
        }
    }

    public void deleteKamar(int id) throws SQLException {
        String sql = "DELETE FROM kamar WHERE id_kamar = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Kamar> getKamarTersedia() throws SQLException {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE status = 'Tersedia' ORDER BY nomor_kamar ASC";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToKamar(rs));
            }
        }
        return list;
    }

    public List<Kamar> searchKamar(String keyword) throws SQLException {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE nomor_kamar LIKE ? OR tipe_kamar LIKE ? OR status LIKE ? ORDER BY nomor_kamar ASC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToKamar(rs));
                }
            }
        }
        return list;
    }

    public int getTotalKamar() throws SQLException {
        String sql = "SELECT COUNT(*) FROM kamar";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getTotalKamarTersedia() throws SQLException {
        String sql = "SELECT COUNT(*) FROM kamar WHERE status = 'Tersedia'";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getTotalKamarTerisi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM kamar WHERE status = 'Terisi'";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Menerapkan Factory Polimorfik: menciptakan instance sesuai tipe kamar (Standard, Deluxe, Suite).
     */
    public static Kamar mapResultSetToKamar(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_kamar");
        String nomor = rs.getString("nomor_kamar");
        String tipe = rs.getString("tipe_kamar");
        double harga = rs.getDouble("harga");
        String status = rs.getString("status");
        int kapasitas = rs.getInt("kapasitas");

        if ("Deluxe".equalsIgnoreCase(tipe)) {
            return new Deluxe(id, nomor, harga, status, kapasitas);
        } else if ("Suite".equalsIgnoreCase(tipe)) {
            return new Suite(id, nomor, harga, status, kapasitas);
        } else {
            return new Standard(id, nomor, harga, status, kapasitas);
        }
    }
}
