package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Class pembantu untuk menguji konektivitas database MySQL
 * sebelum menjalankan aplikasi utama di Eclipse.
 */
public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      UJI KONEKSI DATABASE HOTEL RESERVATION      ");
        System.out.println("==================================================");
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println(">>> STATUS: KONEKSI BERHASIL!");
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("RDBMS       : " + meta.getDatabaseProductName());
                System.out.println("Version     : " + meta.getDatabaseProductVersion());
                System.out.println("URL Driver  : " + meta.getURL());
                System.out.println("User Active : " + meta.getUserName());
                System.out.println("--------------------------------------------------");
                System.out.println("Daftar Tabel yang Terdeteksi:");
                try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("  " + count + ". " + rs.getString("TABLE_NAME"));
                    }
                    if (count == 0) {
                        System.out.println("  (Belum ada tabel. Silakan import database/schema.sql ke phpMyAdmin)");
                    }
                }
                System.out.println("==================================================");
            }
        } catch (Exception e) {
            System.err.println(">>> STATUS: GAGAL TERHUBUNG KE DATABASE!");
            System.err.println("Penyebab: " + e.getMessage());
            System.err.println("Pastikan:");
            System.err.println("1. MySQL di XAMPP sudah di-start (Port 3306).");
            System.err.println("2. Database 'hotel_reservation' sudah dibuat.");
            System.err.println("3. Library mysql-connector-j.jar sudah ada di Build Path Eclipse.");
        }
    }
}
