package dao;

import model.Pembayaran;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Pembayaran.
 * Mengelola riwayat pembayaran, metode pelunasan, dan perhitungan total pendapatan.
 */
public class PembayaranDAO {

    public void tambahPembayaran(Pembayaran p) throws SQLException {
        String sql = "INSERT INTO pembayaran (id_reservasi, tanggal_pembayaran, jumlah_bayar, metode_pembayaran, status_pembayaran) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getIdReservasi());
            stmt.setTimestamp(2, Timestamp.valueOf(p.getTanggalPembayaran()));
            stmt.setDouble(3, p.getJumlahBayar());
            stmt.setString(4, p.getMetodePembayaran());
            stmt.setString(5, p.getStatusPembayaran());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPembayaran(rs.getInt(1));
                }
            }
        }
    }

    public List<Pembayaran> getAllPembayaran() throws SQLException {
        List<Pembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pembayaran ORDER BY id_pembayaran DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPembayaran(rs));
            }
        }
        return list;
    }

    public Pembayaran getPembayaranById(int id) throws SQLException {
        String sql = "SELECT * FROM pembayaran WHERE id_pembayaran = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPembayaran(rs);
                }
            }
        }
        return null;
    }

    public List<Pembayaran> getPembayaranByReservasi(int idReservasi) throws SQLException {
        List<Pembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pembayaran WHERE id_reservasi = ? ORDER BY id_pembayaran DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReservasi);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPembayaran(rs));
                }
            }
        }
        return list;
    }

    public void updatePembayaran(Pembayaran p) throws SQLException {
        String sql = "UPDATE pembayaran SET id_reservasi = ?, tanggal_pembayaran = ?, jumlah_bayar = ?, " +
                     "metode_pembayaran = ?, status_pembayaran = ? WHERE id_pembayaran = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdReservasi());
            stmt.setTimestamp(2, Timestamp.valueOf(p.getTanggalPembayaran()));
            stmt.setDouble(3, p.getJumlahBayar());
            stmt.setString(4, p.getMetodePembayaran());
            stmt.setString(5, p.getStatusPembayaran());
            stmt.setInt(6, p.getIdPembayaran());
            stmt.executeUpdate();
        }
    }

    public void deletePembayaran(int id) throws SQLException {
        String sql = "DELETE FROM pembayaran WHERE id_pembayaran = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Pembayaran> searchPembayaran(String keyword) throws SQLException {
        List<Pembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pembayaran WHERE CAST(id_pembayaran AS CHAR) LIKE ? " +
                     "OR CAST(id_reservasi AS CHAR) LIKE ? OR metode_pembayaran LIKE ? OR status_pembayaran LIKE ? " +
                     "ORDER BY id_pembayaran DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPembayaran(rs));
                }
            }
        }
        return list;
    }

    public double getTotalPendapatan() throws SQLException {
        String sql = "SELECT SUM(jumlah_bayar) FROM pembayaran WHERE status_pembayaran = 'Lunas' OR status_pembayaran = 'DP'";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    private Pembayaran mapResultSetToPembayaran(ResultSet rs) throws SQLException {
        return new Pembayaran(
            rs.getInt("id_pembayaran"),
            rs.getInt("id_reservasi"),
            rs.getTimestamp("tanggal_pembayaran").toLocalDateTime(),
            rs.getDouble("jumlah_bayar"),
            rs.getString("metode_pembayaran"),
            rs.getString("status_pembayaran")
        );
    }
}
