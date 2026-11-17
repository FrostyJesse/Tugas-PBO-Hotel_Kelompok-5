package dao;

import model.Deluxe;
import model.Kamar;
import model.Reservasi;
import model.Standard;
import model.Suite;
import model.Tamu;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Reservasi.
 * Mengelola transaksi pemesanan dengan relasi join ke Tamu dan Kamar.
 */
public class ReservasiDAO {

    private static final String SELECT_JOIN_SQL = 
        "SELECT r.*, " +
        "t.nama, t.nomor_identitas, t.no_telepon, t.email, t.alamat, " +
        "k.nomor_kamar, k.tipe_kamar, k.harga, k.status AS status_kamar, k.kapasitas " +
        "FROM reservasi r " +
        "INNER JOIN tamu t ON r.id_tamu = t.id_tamu " +
        "INNER JOIN kamar k ON r.id_kamar = k.id_kamar ";

    public void tambahReservasi(Reservasi r) throws SQLException {
        String sql = "INSERT INTO reservasi (id_tamu, id_kamar, tanggal_checkin, tanggal_checkout, jumlah_tamu, status_reservasi, total_harga) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, r.getIdTamu());
            stmt.setInt(2, r.getIdKamar());
            stmt.setDate(3, Date.valueOf(r.getTanggalCheckin()));
            stmt.setDate(4, Date.valueOf(r.getTanggalCheckout()));
            stmt.setInt(5, r.getJumlahTamu());
            stmt.setString(6, r.getStatusReservasi());
            stmt.setDouble(7, r.getTotalHarga());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setIdReservasi(rs.getInt(1));
                }
            }
        }
    }

    public List<Reservasi> getAllReservasi() throws SQLException {
        List<Reservasi> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + "ORDER BY r.id_reservasi DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToReservasi(rs));
            }
        }
        return list;
    }

    public Reservasi getReservasiById(int id) throws SQLException {
        String sql = SELECT_JOIN_SQL + "WHERE r.id_reservasi = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservasi(rs);
                }
            }
        }
        return null;
    }

    public void updateReservasi(Reservasi r) throws SQLException {
        String sql = "UPDATE reservasi SET id_tamu = ?, id_kamar = ?, tanggal_checkin = ?, " +
                     "tanggal_checkout = ?, jumlah_tamu = ?, status_reservasi = ?, total_harga = ? " +
                     "WHERE id_reservasi = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getIdTamu());
            stmt.setInt(2, r.getIdKamar());
            stmt.setDate(3, Date.valueOf(r.getTanggalCheckin()));
            stmt.setDate(4, Date.valueOf(r.getTanggalCheckout()));
            stmt.setInt(5, r.getJumlahTamu());
            stmt.setString(6, r.getStatusReservasi());
            stmt.setDouble(7, r.getTotalHarga());
            stmt.setInt(8, r.getIdReservasi());
            stmt.executeUpdate();
        }
    }

    public void updateStatusReservasi(int idReservasi, String status) throws SQLException {
        String sql = "UPDATE reservasi SET status_reservasi = ? WHERE id_reservasi = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idReservasi);
            stmt.executeUpdate();
        }
    }

    public void deleteReservasi(int id) throws SQLException {
        String sql = "DELETE FROM reservasi WHERE id_reservasi = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Reservasi> searchReservasi(String keyword) throws SQLException {
        List<Reservasi> list = new ArrayList<>();
        String sql = SELECT_JOIN_SQL + 
                     "WHERE CAST(r.id_reservasi AS CHAR) LIKE ? OR t.nama LIKE ? OR k.nomor_kamar LIKE ? OR r.status_reservasi LIKE ? " +
                     "ORDER BY r.id_reservasi DESC";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReservasi(rs));
                }
            }
        }
        return list;
    }

    public int getTotalReservasi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservasi";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Reservasi mapResultSetToReservasi(ResultSet rs) throws SQLException {
        int idTamu = rs.getInt("id_tamu");
        Tamu tamu = new Tamu(
            idTamu,
            rs.getString("nama"),
            rs.getString("nomor_identitas"),
            rs.getString("no_telepon"),
            rs.getString("email"),
            rs.getString("alamat")
        );

        String tipe = rs.getString("tipe_kamar");
        int idKamar = rs.getInt("id_kamar");
        String noKamar = rs.getString("nomor_kamar");
        double harga = rs.getDouble("harga");
        String statusKamar = rs.getString("status_kamar");
        int kapasitas = rs.getInt("kapasitas");

        Kamar kamar;
        if ("Deluxe".equalsIgnoreCase(tipe)) {
            kamar = new Deluxe(idKamar, noKamar, harga, statusKamar, kapasitas);
        } else if ("Suite".equalsIgnoreCase(tipe)) {
            kamar = new Suite(idKamar, noKamar, harga, statusKamar, kapasitas);
        } else {
            kamar = new Standard(idKamar, noKamar, harga, statusKamar, kapasitas);
        }

        Reservasi r = new Reservasi(
            rs.getInt("id_reservasi"),
            idTamu,
            idKamar,
            rs.getDate("tanggal_checkin").toLocalDate(),
            rs.getDate("tanggal_checkout").toLocalDate(),
            rs.getInt("jumlah_tamu"),
            rs.getString("status_reservasi"),
            rs.getDouble("total_harga")
        );
        r.setTamu(tamu);
        r.setKamar(kamar);
        return r;
    }
}
