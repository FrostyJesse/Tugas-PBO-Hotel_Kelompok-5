package view;

import model.User;
import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * Frame Utama Aplikasi Hotel Reservation System.
 * Mengintegrasikan JMenuBar, Sidebar Navigasi, dan CardLayout
 * untuk menampilkan halaman Dashboard, Tamu, Kamar, Reservasi, dan Pembayaran.
 */
public class MainDashboardView extends JFrame {
    private static final long serialVersionUID = 1L;

    private User currentUser;

    // Card Layout Container
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Sub-views
    private DashboardView dashboardView;
    private TamuView tamuView;
    private KamarView kamarView;
    private ReservasiView reservasiView;
    private PembayaranView pembayaranView;

    // Tombol Navigasi Sidebar
    private JButton btnNavDashboard;
    private JButton btnNavTamu;
    private JButton btnNavKamar;
    private JButton btnNavReservasi;
    private JButton btnNavPembayaran;
    private JButton btnNavLogout;

    // Menu Bar Items
    private JMenuItem miDashboard;
    private JMenuItem miTamu;
    private JMenuItem miKamar;
    private JMenuItem miReservasi;
    private JMenuItem miPembayaran;
    private JMenuItem miLogout;
    private JMenuItem miAbout;

    public MainDashboardView(User user) {
        this.currentUser = user;
        setTitle("Hotel Reservation System - Petugas: " + (user != null ? user.getUsername() : "Guest"));
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        initComponents();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu File
        JMenu menuFile = new JMenu("File");
        miDashboard = new JMenuItem("Dashboard Utama");
        miLogout = new JMenuItem("Logout Sistem");
        JMenuItem miExit = new JMenuItem("Keluar Aplikasi");
        miExit.addActionListener(e -> System.exit(0));

        menuFile.add(miDashboard);
        menuFile.addSeparator();
        menuFile.add(miLogout);
        menuFile.add(miExit);

        // Menu Master Data
        JMenu menuMaster = new JMenu("Master Data");
        miTamu = new JMenuItem("Data Tamu");
        miKamar = new JMenuItem("Data Kamar");
        menuMaster.add(miTamu);
        menuMaster.add(miKamar);

        // Menu Transaksi
        JMenu menuTransaksi = new JMenu("Transaksi");
        miReservasi = new JMenuItem("Reservasi & Check-in/out");
        miPembayaran = new JMenuItem("Pembayaran Tagihan");
        menuTransaksi.add(miReservasi);
        menuTransaksi.add(miPembayaran);

        // Menu Bantuan / Info
        JMenu menuHelp = new JMenu("Bantuan");
        miAbout = new JMenuItem("Tentang Aplikasi");
        menuHelp.add(miAbout);

        menuBar.add(menuFile);
        menuBar.add(menuMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());

        // 1. Sidebar Navigasi Kiri
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_NAVY);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo / Title Brand
        JLabel lblBrand = new JLabel("HOTEL SYSTEM");
        lblBrand.setFont(UIUtils.FONT_HEADER);
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Management Desktop");
        lblSub.setFont(UIUtils.FONT_REGULAR);
        lblSub.setForeground(new Color(189, 195, 199));
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Tombol Navigasi
        btnNavDashboard = createSidebarButton("📊  Dashboard");
        btnNavTamu = createSidebarButton("👥  Data Tamu");
        btnNavKamar = createSidebarButton("🛏  Data Kamar");
        btnNavReservasi = createSidebarButton("📅  Reservasi");
        btnNavPembayaran = createSidebarButton("💳  Pembayaran");
        btnNavLogout = createSidebarButton("🚪  Logout");
        btnNavLogout.setBackground(new Color(192, 57, 43));

        sidebar.add(btnNavDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavTamu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavKamar);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavReservasi);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNavPembayaran);

        sidebar.add(Box.createVerticalGlue());

        // Info User Login
        String namaPetugas = (currentUser != null) ? currentUser.getUsername() : "Petugas";
        String rolePetugas = (currentUser != null) ? currentUser.getRole() : "Staff";

        JPanel userPanel = new JPanel(new BorderLayout(5, 2));
        userPanel.setBackground(UIUtils.COLOR_NAVY_LIGHT);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblUserTitle = new JLabel("Petugas Aktif:");
        lblUserTitle.setFont(UIUtils.FONT_REGULAR);
        lblUserTitle.setForeground(new Color(189, 195, 199));

        JLabel lblUserName = new JLabel(namaPetugas + " (" + rolePetugas + ")");
        lblUserName.setFont(UIUtils.FONT_BOLD);
        lblUserName.setForeground(Color.WHITE);

        userPanel.add(lblUserTitle, BorderLayout.NORTH);
        userPanel.add(lblUserName, BorderLayout.SOUTH);

        sidebar.add(userPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnNavLogout);

        // 2. Main Content Panel (CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIUtils.COLOR_BG);

        dashboardView = new DashboardView();
        tamuView = new TamuView();
        kamarView = new KamarView();
        reservasiView = new ReservasiView();
        pembayaranView = new PembayaranView();

        mainContentPanel.add(dashboardView, "DASHBOARD");
        mainContentPanel.add(tamuView, "TAMU");
        mainContentPanel.add(kamarView, "KAMAR");
        mainContentPanel.add(reservasiView, "RESERVASI");
        mainContentPanel.add(pembayaranView, "PEMBAYARAN");

        rootPanel.add(sidebar, BorderLayout.WEST);
        rootPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(rootPanel);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setFont(UIUtils.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(UIUtils.COLOR_NAVY_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(JButton.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        return btn;
    }

    public void showCard(String cardName) {
        cardLayout.show(mainContentPanel, cardName);
    }

    // Getters View
    public DashboardView getDashboardView() { return dashboardView; }
    public TamuView getTamuView() { return tamuView; }
    public KamarView getKamarView() { return kamarView; }
    public ReservasiView getReservasiView() { return reservasiView; }
    public PembayaranView getPembayaranView() { return pembayaranView; }
    public User getCurrentUser() { return currentUser; }

    // Event Registrations
    public void addNavDashboardListener(ActionListener l) {
        btnNavDashboard.addActionListener(l);
        miDashboard.addActionListener(l);
    }
    public void addNavTamuListener(ActionListener l) {
        btnNavTamu.addActionListener(l);
        miTamu.addActionListener(l);
    }
    public void addNavKamarListener(ActionListener l) {
        btnNavKamar.addActionListener(l);
        miKamar.addActionListener(l);
    }
    public void addNavReservasiListener(ActionListener l) {
        btnNavReservasi.addActionListener(l);
        miReservasi.addActionListener(l);
    }
    public void addNavPembayaranListener(ActionListener l) {
        btnNavPembayaran.addActionListener(l);
        miPembayaran.addActionListener(l);
    }
    public void addNavLogoutListener(ActionListener l) {
        btnNavLogout.addActionListener(l);
        miLogout.addActionListener(l);
    }
    public void addAboutListener(ActionListener l) {
        miAbout.addActionListener(l);
    }
}
