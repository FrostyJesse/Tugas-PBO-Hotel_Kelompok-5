package controller;

import dao.KamarDAO;
import dao.PembayaranDAO;
import dao.ReservasiDAO;
import dao.TamuDAO;
import dao.UserDAO;
import view.LoginView;
import view.MainDashboardView;

import javax.swing.JOptionPane;
import java.sql.SQLException;

/**
 * Controller utama aplikasi yang mengatur navigasi antar modul,
 * menginisialisasi sub-controller, dan memperbarui metrik statistik dashboard.
 */
public class DashboardController {
    private MainDashboardView mainView;

    // DAOs
    private TamuDAO tamuDAO;
    private KamarDAO kamarDAO;
    private ReservasiDAO reservasiDAO;
    private PembayaranDAO pembayaranDAO;

    // Sub Controllers
    private TamuController tamuController;
    private KamarController kamarController;
    private ReservasiController reservasiController;
    private PembayaranController pembayaranController;

    public DashboardController(MainDashboardView mainView) {
        this.mainView = mainView;

        // Inisialisasi DAO
        this.tamuDAO = new TamuDAO();
        this.kamarDAO = new KamarDAO();
        this.reservasiDAO = new ReservasiDAO();
        this.pembayaranDAO = new PembayaranDAO();

        // Inisialisasi Sub-Controller
        this.tamuController = new TamuController(mainView.getTamuView(), tamuDAO);
        this.kamarController = new KamarController(mainView.getKamarView(), kamarDAO);
        this.reservasiController = new ReservasiController(mainView.getReservasiView(), reservasiDAO, tamuDAO, kamarDAO);
        this.pembayaranController = new PembayaranController(mainView.getPembayaranView(), pembayaranDAO, reservasiDAO);

        initNavigation();
        refreshDashboardMetrics();
    }

    private void initNavigation() {
        mainView.addNavDashboardListener(e -> {
            mainView.showCard("DASHBOARD");
            refreshDashboardMetrics();
        });

        mainView.addNavTamuListener(e -> {
            mainView.showCard("TAMU");
            tamuController.loadAllData();
        });

        mainView.addNavKamarListener(e -> {
            mainView.showCard("KAMAR");
            kamarController.loadAllData();
        });

        mainView.addNavReservasiListener(e -> {
            mainView.showCard("RESERVASI");
            reservasiController.loadComboData();
            reservasiController.loadAllData();
        });

        mainView.addNavPembayaranListener(e -> {
            mainView.showCard("PEMBAYARAN");
            pembayaranController.loadComboReservasi();
            pembayaranController.loadAllData();
        });

        mainView.addNavLogoutListener(e -> performLogout());

        mainView.addAboutListener(e -> showAboutDialog());
    }

    public void refreshDashboardMetrics() {
        try {
            int totalTamu = tamuDAO.getTotalTamu();
            int totalKamar = kamarDAO.getTotalKamar();
            int kamarTersedia = kamarDAO.getTotalKamarTersedia();
            int kamarTerisi = kamarDAO.getTotalKamarTerisi();
            int totalReservasi = reservasiDAO.getTotalReservasi();
            double totalPendapatan = pembayaranDAO.getTotalPendapatan();

            mainView.getDashboardView().updateMetrics(
                totalTamu,
                totalKamar,
                kamarTersedia,
                kamarTerisi,
                totalReservasi,
                totalPendapatan
            );
        } catch (SQLException ex) {
            System.err.println("Gagal memuat metrik dashboard: " + ex.getMessage());
        }
    }

    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            mainView,
            "Apakah Anda yakin ingin keluar (Logout) dari sistem?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            mainView.dispose();
            LoginView loginView = new LoginView();
            new LoginController(loginView, new UserDAO());
            loginView.setVisible(true);
        }
    }

    private void showAboutDialog() {
        String info = 
            "=====================================================\n" +
            "       HOTEL RESERVATION SYSTEM (PBO PROJECT)        \n" +
            "=====================================================\n" +
            "Arsitektur   : Model-View-Controller (MVC) + DAO\n" +
            "Bahasa/GUI   : Java Swing Desktop\n" +
            "Basis Data   : MySQL via JDBC Driver\n" +
            "Pilar OOP    : Encapsulation, Inheritance, Abstraction, Polymorphism\n" +
            "Fitur Utama  : CRUD Tamu & Kamar, Reservasi Polimorfik,\n" +
            "               Diskon Menginap, Check-In, Check-Out,\n" +
            "               Pembayaran & Cetak Kwitansi.\n" +
            "=====================================================";
        JOptionPane.showMessageDialog(mainView, info, "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE);
    }
}
