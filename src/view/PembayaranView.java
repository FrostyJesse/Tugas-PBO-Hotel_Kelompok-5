package view;

import model.Reservasi;
import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * View Panel untuk Transaksi Pembayaran Reservasi Hotel.
 * Menggunakan kombinasi JRadioButton, JComboBox, JTextField, dan JTable.
 */
public class PembayaranView extends JPanel {
    private static final long serialVersionUID = 1L;

    // Form Input
    private JTextField txtIdPembayaran;
    private JComboBox<Reservasi> cmbReservasi;
    private JTextField txtTagihan;
    private JTextField txtJumlahBayar;
    private JComboBox<String> cmbStatusBayar;

    // JRadioButton untuk Metode Pembayaran (variasi komponen Swing)
    private JRadioButton rbCash;
    private JRadioButton rbTransfer;
    private JRadioButton rbDebit;
    private JRadioButton rbQRIS;
    private ButtonGroup bgMetode;

    // Tombol Form
    private JButton btnBayar;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnStruk;

    // Search Bar
    private JTextField txtCari;
    private JButton btnCari;
    private JButton btnRefresh;

    // JTable
    private JTable tblPembayaran;
    private DefaultTableModel tableModel;

    public PembayaranView() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BG);
        JLabel lblTitle = new JLabel("TRANSAKSI PEMBAYARAN HOTEL");
        lblTitle.setFont(UIUtils.FONT_HEADER);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Form Card (Kiri)
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdPembayaran = new JTextField();
        txtIdPembayaran.setVisible(false);

        cmbReservasi = new JComboBox<>();
        txtTagihan = new JTextField(15);
        txtTagihan.setEditable(false);
        txtTagihan.setBackground(new Color(245, 245, 245));

        txtJumlahBayar = new JTextField(15);

        // Radio Button Metode Pembayaran
        rbCash = new JRadioButton("Cash", true);
        rbTransfer = new JRadioButton("Transfer");
        rbDebit = new JRadioButton("Debit");
        rbQRIS = new JRadioButton("QRIS");

        rbCash.setBackground(UIUtils.COLOR_CARD_BG);
        rbTransfer.setBackground(UIUtils.COLOR_CARD_BG);
        rbDebit.setBackground(UIUtils.COLOR_CARD_BG);
        rbQRIS.setBackground(UIUtils.COLOR_CARD_BG);

        bgMetode = new ButtonGroup();
        bgMetode.add(rbCash);
        bgMetode.add(rbTransfer);
        bgMetode.add(rbDebit);
        bgMetode.add(rbQRIS);

        JPanel pnlMetode = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlMetode.setBackground(UIUtils.COLOR_CARD_BG);
        pnlMetode.add(rbCash);
        pnlMetode.add(rbTransfer);
        pnlMetode.add(rbDebit);
        pnlMetode.add(rbQRIS);

        cmbStatusBayar = new JComboBox<>(new String[]{"Lunas", "DP", "Belum Lunas"});

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; formCard.add(new JLabel("Pilih Reservasi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; formCard.add(cmbReservasi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Total Tagihan:"), gbc);
        gbc.gridx = 1; formCard.add(txtTagihan, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Jumlah Bayar (Rp):"), gbc);
        gbc.gridx = 1; formCard.add(txtJumlahBayar, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Metode Bayar:"), gbc);
        gbc.gridx = 1; formCard.add(pnlMetode, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Status Bayar:"), gbc);
        gbc.gridx = 1; formCard.add(cmbStatusBayar, gbc);

        // Tombol Form
        row++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel.setBackground(UIUtils.COLOR_CARD_BG);

        btnBayar = new JButton("Proses Bayar");
        UIUtils.styleButton(btnBayar, UIUtils.COLOR_SUCCESS, Color.WHITE);

        btnStruk = new JButton("Cetak Kwitansi");
        UIUtils.styleButton(btnStruk, UIUtils.COLOR_PRIMARY, Color.WHITE);

        btnHapus = new JButton("Hapus");
        UIUtils.styleButton(btnHapus, UIUtils.COLOR_DANGER, Color.WHITE);

        btnReset = new JButton("Reset");
        UIUtils.styleButton(btnReset, Color.GRAY, Color.WHITE);

        btnPanel.add(btnBayar);
        btnPanel.add(btnStruk);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(btnPanel, gbc);

        // Panel Tabel (Kanan)
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setBackground(UIUtils.COLOR_CARD_BG);
        searchPanel.add(new JLabel("Cari Pembayaran:"));
        txtCari = new JTextField(16);
        searchPanel.add(txtCari);

        btnCari = new JButton("Cari");
        UIUtils.styleButton(btnCari, UIUtils.COLOR_PRIMARY, Color.WHITE);
        searchPanel.add(btnCari);

        btnRefresh = new JButton("Refresh");
        UIUtils.styleButton(btnRefresh, Color.GRAY, Color.WHITE);
        searchPanel.add(btnRefresh);

        String[] cols = {"ID Bayar", "ID Reservasi", "Waktu Pembayaran", "Jumlah (Rp)", "Metode", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblPembayaran = new JTable(tableModel);
        UIUtils.styleTable(tblPembayaran);
        JScrollPane scrollPane = new JScrollPane(tblPembayaran);

        tableCard.add(searchPanel, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(UIUtils.COLOR_BG);
        contentPanel.add(formCard, BorderLayout.WEST);
        contentPanel.add(tableCard, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Getters & Setters
    public String getIdPembayaran() { return txtIdPembayaran.getText().trim(); }
    public void setIdPembayaran(String id) { txtIdPembayaran.setText(id); }

    public Reservasi getSelectedReservasi() { return (Reservasi) cmbReservasi.getSelectedItem(); }
    public JComboBox<Reservasi> getCmbReservasi() { return cmbReservasi; }

    public void setTagihan(double tagihan) {
        txtTagihan.setText("Rp " + String.format("%,.0f", tagihan));
    }

    public String getJumlahBayar() { return txtJumlahBayar.getText().trim(); }
    public void setJumlahBayar(String jml) { txtJumlahBayar.setText(jml); }

    public String getMetodePembayaran() {
        if (rbCash.isSelected()) return "Cash";
        if (rbTransfer.isSelected()) return "Transfer";
        if (rbDebit.isSelected()) return "Debit";
        if (rbQRIS.isSelected()) return "QRIS";
        return "Cash";
    }

    public void setMetodePembayaran(String metode) {
        if ("Transfer".equalsIgnoreCase(metode)) rbTransfer.setSelected(true);
        else if ("Debit".equalsIgnoreCase(metode)) rbDebit.setSelected(true);
        else if ("QRIS".equalsIgnoreCase(metode)) rbQRIS.setSelected(true);
        else rbCash.setSelected(true);
    }

    public String getStatusPembayaran() { return (String) cmbStatusBayar.getSelectedItem(); }
    public void setStatusPembayaran(String status) { cmbStatusBayar.setSelectedItem(status); }

    public String getSearchKeyword() { return txtCari.getText().trim(); }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return tblPembayaran; }

    public void clearForm() {
        txtIdPembayaran.setText("");
        if (cmbReservasi.getItemCount() > 0) cmbReservasi.setSelectedIndex(0);
        txtJumlahBayar.setText("");
        rbCash.setSelected(true);
        cmbStatusBayar.setSelectedIndex(0);
    }

    public void addBayarListener(ActionListener l) { btnBayar.addActionListener(l); }
    public void addStrukListener(ActionListener l) { btnStruk.addActionListener(l); }
    public void addHapusListener(ActionListener l) { btnHapus.addActionListener(l); }
    public void addResetListener(ActionListener l) { btnReset.addActionListener(l); }
    public void addCariListener(ActionListener l) { btnCari.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { btnRefresh.addActionListener(l); }
    public void addReservasiSelectListener(ActionListener l) { cmbReservasi.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter m) { tblPembayaran.addMouseListener(m); }
}
