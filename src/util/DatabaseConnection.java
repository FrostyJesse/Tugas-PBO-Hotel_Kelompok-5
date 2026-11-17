package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class untuk mengelola koneksi database MySQL menggunakan JDBC.
 * Mengikuti prinsip Singleton pattern agar koneksi terpusat dan efisien.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_reservation?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // default XAMPP MySQL tanpa password

    private static Connection connection = null;

    // Private constructor agar class tidak dapat diinstansiasi langsung
    private DatabaseConnection() {}

    /**
     * Mendapatkan objek koneksi ke database MySQL.
     * @return Connection objek koneksi aktif
     * @throws SQLException jika koneksi gagal dibuka
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Mendaftarkan MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan. Pastikan mysql-connector-j.jar sudah ditambahkan ke classpath.", e);
        }
        return connection;
    }

    /**
     * Menutup koneksi database jika sedang terbuka.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi database: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
