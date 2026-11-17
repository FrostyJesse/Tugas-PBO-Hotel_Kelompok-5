package view;

import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * View Panel untuk Tampilan Utama Dashboard Metrik Statistik Hotel.
 * Menampilkan ringkasan: Total Tamu, Total Kamar, Kamar Tersedia,
 * Kamar Terisi, Total Reservasi, dan Total Pendapatan.
 */
public class DashboardView extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel lblTotalTamu;
    private JLabel lblTotalKamar;
    private JLabel lblKamarTersedia;
    private JLabel lblKamarTerisi;
    private JLabel lblTotalReservasi;
    private JLabel lblTotalPendapatan;

    public DashboardView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // Welcome Header Banner
        JPanel headerPanel = UIUtils.createCardPanel();
        headerPanel.setLayout(new BorderLayout(10, 5));
        headerPanel.setBackground(UIUtils.COLOR_NAVY);

        JLabel lblWelcome = new JLabel("SELAMAT DATANG DI HOTEL RESERVATION SYSTEM");
        lblWelcome.setFont(UIUtils.FONT_HEADER);
        lblWelcome.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("Sistem Manajemen Operasional Tamu, Kamar, Reservasi, Check-In, Check-Out, dan Keuangan Hotel");
        lblDesc.setFont(UIUtils.FONT_REGULAR);
        lblDesc.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblWelcome, BorderLayout.NORTH);
        headerPanel.add(lblDesc, BorderLayout.SOUTH);

        // Grid 6 Kartu Metrik Statistik
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        statsGrid.setBackground(UIUtils.COLOR_BG);

        lblTotalTamu = new JLabel("0", JLabel.CENTER);
        lblTotalKamar = new JLabel("0", JLabel.CENTER);
        lblKamarTersedia = new JLabel("0", JLabel.CENTER);
        lblKamarTerisi = new JLabel("0", JLabel.CENTER);
        lblTotalReservasi = new JLabel("0", JLabel.CENTER);
        lblTotalPendapatan = new JLabel("Rp 0", JLabel.CENTER);

        statsGrid.add(createMetricCard("TOTAL TAMU TERDAFTAR", lblTotalTamu, new Color(41, 128, 185)));
        statsGrid.add(createMetricCard("TOTAL JUMLAH KAMAR", lblTotalKamar, new Color(142, 68, 173)));
        statsGrid.add(createMetricCard("KAMAR TERSEDIA", lblKamarTersedia, UIUtils.COLOR_SUCCESS));
        statsGrid.add(createMetricCard("KAMAR TERISI (OCCUPIED)", lblKamarTerisi, UIUtils.COLOR_DANGER));
        statsGrid.add(createMetricCard("TOTAL TRANSAKSI RESERVASI", lblTotalReservasi, UIUtils.COLOR_WARNING));
        statsGrid.add(createMetricCard("TOTAL PENDAPATAN DITERIMA", lblTotalPendapatan, new Color(22, 160, 133)));

        add(headerPanel, BorderLayout.NORTH);
        add(statsGrid, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(5, 10));

        // Border aksen di bagian atas kartu
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(4, 0, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(UIUtils.FONT_BOLD);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        valueLabel.setFont(UIUtils.FONT_METRIC);
        valueLabel.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void updateMetrics(int tamu, int kamar, int tersedia, int terisi, int reservasi, double pendapatan) {
        lblTotalTamu.setText(String.valueOf(tamu));
        lblTotalKamar.setText(String.valueOf(kamar));
        lblKamarTersedia.setText(String.valueOf(tersedia));
        lblKamarTerisi.setText(String.valueOf(terisi));
        lblTotalReservasi.setText(String.valueOf(reservasi));
        lblTotalPendapatan.setText("Rp " + String.format("%,.0f", pendapatan));
    }
}
