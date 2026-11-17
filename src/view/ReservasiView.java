package view;

import model.Kamar;
import model.Tamu;
import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * View Panel untuk Manajemen Reservasi Kamar Hotel.
 * Dilengkapi Date Spinner, Kalkulasi Otomatis Tarif Polimorfik,
 * serta tombol aksi Check-In dan Check-Out.
 */
public class ReservasiView extends JPanel {
    private static final long serialVersionUID = 1L;

    // Form Input
    private JTextField txtIdReservasi;
    private JComboBox<Tamu> cmbTamu;
    private JComboBox<Kamar> cmbKamar;
    private JSpinner spnrCheckin;
    private JSpinner spnrCheckout;
    private JSpinner spnrJumlahTamu;
    private JComboBox<String> cmbStatusReservasi;

    // Komponen Ringkasan Kalkulasi Tarif Real-Time
    private JLabel lblDetailKamar;
    private JLabel lblJumlahMalam;
    private JLabel lblSubtotal;
    private JLabel lblDiskon;
    private JLabel lblTotalAkhir;

    // Tombol CRUD & Operasional
    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnCheckIn;
    private JButton btnCheckOut;

    // Search Bar
    private JTextField txtCari;
    private JButton btnCari;
    private JButton btnRefresh;

    // JTable
    private JTable tblReservasi;
    private DefaultTableModel tableModel;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ReservasiView() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
    }

    private void initComponents() {
        // 1. Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BG);
        JLabel lblTitle = new JLabel("MANAJEMEN RESERVASI & OPERASIONAL HOTEL");
        lblTitle.setFont(UIUtils.FONT_HEADER);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // 2. Panel Form (Kiri)
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdReservasi = new JTextField();
        txtIdReservasi.setVisible(false);

        cmbTamu = new JComboBox<>();
        cmbKamar = new JComboBox<>();

        // Date Spinner Check-in (hari ini)
        Date today = new Date();
        spnrCheckin = new JSpinner(new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH));
        spnrCheckin.setEditor(new JSpinner.DateEditor(spnrCheckin, "yyyy-MM-dd"));

        // Date Spinner Check-out (besok)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();
        spnrCheckout = new JSpinner(new SpinnerDateModel(tomorrow, null, null, Calendar.DAY_OF_MONTH));
        spnrCheckout.setEditor(new JSpinner.DateEditor(spnrCheckout, "yyyy-MM-dd"));

        spnrJumlahTamu = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        cmbStatusReservasi = new JComboBox<>(new String[]{"PENDING", "CHECKED_IN", "CHECKED_OUT", "BATAL"});

        // Panel Kalkulasi Ringkasan Tarif
        JPanel kalkulasiPanel = new JPanel(new GridBagLayout());
        kalkulasiPanel.setBackground(new Color(240, 248, 255));
        kalkulasiPanel.setBorder(BorderFactory.createTitledBorder("Rincian Tarif & Polimorfisme"));
        GridBagConstraints kgbc = new GridBagConstraints();
        kgbc.insets = new Insets(3, 6, 3, 6);
        kgbc.fill = GridBagConstraints.HORIZONTAL;

        lblDetailKamar = new JLabel("Tipe Kamar: -");
        lblJumlahMalam = new JLabel("Durasi: 1 malam");
        lblSubtotal = new JLabel("Subtotal: Rp 0");
        lblDiskon = new JLabel("Diskon: Rp 0 (0%)");
        lblTotalAkhir = new JLabel("TOTAL: Rp 0");
        lblTotalAkhir.setFont(UIUtils.FONT_BOLD);
        lblTotalAkhir.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        kgbc.gridx = 0; kgbc.gridy = 0; kgbc.weightx = 1.0; kalkulasiPanel.add(lblDetailKamar, kgbc);
        kgbc.gridy = 1; kalkulasiPanel.add(lblJumlahMalam, kgbc);
        kgbc.gridy = 2; kalkulasiPanel.add(lblSubtotal, kgbc);
        kgbc.gridy = 3; kalkulasiPanel.add(lblDiskon, kgbc);
        kgbc.gridy = 4; kalkulasiPanel.add(lblTotalAkhir, kgbc);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; formCard.add(new JLabel("Pilih Tamu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; formCard.add(cmbTamu, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Pilih Kamar:"), gbc);
        gbc.gridx = 1; formCard.add(cmbKamar, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Tgl Check-In:"), gbc);
        gbc.gridx = 1; formCard.add(spnrCheckin, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Tgl Check-Out:"), gbc);
        gbc.gridx = 1; formCard.add(spnrCheckout, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Jumlah Tamu:"), gbc);
        gbc.gridx = 1; formCard.add(spnrJumlahTamu, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formCard.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; formCard.add(cmbStatusReservasi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(kalkulasiPanel, gbc);

        // Tombol Form CRUD
        row++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        btnPanel.setBackground(UIUtils.COLOR_CARD_BG);

        btnSimpan = new JButton("Simpan");
        UIUtils.styleButton(btnSimpan, UIUtils.COLOR_SUCCESS, Color.WHITE);

        btnUpdate = new JButton("Update");
        UIUtils.styleButton(btnUpdate, UIUtils.COLOR_WARNING, Color.WHITE);

        btnHapus = new JButton("Hapus");
        UIUtils.styleButton(btnHapus, UIUtils.COLOR_DANGER, Color.WHITE);

        btnReset = new JButton("Reset");
        UIUtils.styleButton(btnReset, Color.GRAY, Color.WHITE);

        btnPanel.add(btnSimpan);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(btnPanel, gbc);

        // Tombol Operasional Check-In & Check-Out
        row++;
        JPanel operasionalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        operasionalPanel.setBackground(UIUtils.COLOR_CARD_BG);

        btnCheckIn = new JButton("► Check-In Kamar");
        UIUtils.styleButton(btnCheckIn, UIUtils.COLOR_PRIMARY, Color.WHITE);

        btnCheckOut = new JButton("✔ Check-Out & Tagihan");
        UIUtils.styleButton(btnCheckOut, new Color(142, 68, 173), Color.WHITE); // Ungu

        operasionalPanel.add(btnCheckIn);
        operasionalPanel.add(btnCheckOut);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(operasionalPanel, gbc);

        // 3. Panel Tabel & Search (Kanan)
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setBackground(UIUtils.COLOR_CARD_BG);
        searchPanel.add(new JLabel("Cari Reservasi:"));
        txtCari = new JTextField(16);
        searchPanel.add(txtCari);

        btnCari = new JButton("Cari");
        UIUtils.styleButton(btnCari, UIUtils.COLOR_PRIMARY, Color.WHITE);
        searchPanel.add(btnCari);

        btnRefresh = new JButton("Refresh");
        UIUtils.styleButton(btnRefresh, Color.GRAY, Color.WHITE);
        searchPanel.add(btnRefresh);

        String[] cols = {"ID", "Tamu", "No. Kamar", "Tipe", "Check-in", "Check-out", "Malam", "Tamu", "Status", "Total (Rp)"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblReservasi = new JTable(tableModel);
        UIUtils.styleTable(tblReservasi);
        JScrollPane scrollPane = new JScrollPane(tblReservasi);

        tableCard.add(searchPanel, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(UIUtils.COLOR_BG);
        contentPanel.add(formCard, BorderLayout.WEST);
        contentPanel.add(tableCard, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS, SETTERS & DATE HELPERS
    // ==========================================

    public String getIdReservasi() { return txtIdReservasi.getText().trim(); }
    public void setIdReservasi(String id) { txtIdReservasi.setText(id); }

    public Tamu getSelectedTamu() { return (Tamu) cmbTamu.getSelectedItem(); }
    public void setSelectedTamu(Tamu t) { cmbTamu.setSelectedItem(t); }

    public Kamar getSelectedKamar() { return (Kamar) cmbKamar.getSelectedItem(); }
    public void setSelectedKamar(Kamar k) { cmbKamar.setSelectedItem(k); }

    public JComboBox<Tamu> getCmbTamu() { return cmbTamu; }
    public JComboBox<Kamar> getCmbKamar() { return cmbKamar; }

    public LocalDate getCheckinDate() {
        Date d = (Date) spnrCheckin.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setCheckinDate(LocalDate date) {
        Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        spnrCheckin.setValue(d);
    }

    public LocalDate getCheckoutDate() {
        Date d = (Date) spnrCheckout.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setCheckoutDate(LocalDate date) {
        Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        spnrCheckout.setValue(d);
    }

    public int getJumlahTamu() { return (Integer) spnrJumlahTamu.getValue(); }
    public void setJumlahTamu(int jml) { spnrJumlahTamu.setValue(jml); }

    public String getStatusReservasi() { return (String) cmbStatusReservasi.getSelectedItem(); }
    public void setStatusReservasi(String s) { cmbStatusReservasi.setSelectedItem(s); }

    public String getSearchKeyword() { return txtCari.getText().trim(); }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return tblReservasi; }

    public void updateKalkulasiLabel(String tipe, int malam, double subtotal, double diskon, double total) {
        lblDetailKamar.setText("Tipe Kamar: " + tipe);
        lblJumlahMalam.setText("Durasi: " + malam + " malam");
        lblSubtotal.setText("Subtotal: Rp " + String.format("%,.0f", subtotal));
        lblDiskon.setText("Diskon Durasi: - Rp " + String.format("%,.0f", diskon));
        lblTotalAkhir.setText("TOTAL: Rp " + String.format("%,.0f", total));
    }

    public void clearForm() {
        txtIdReservasi.setText("");
        if (cmbTamu.getItemCount() > 0) cmbTamu.setSelectedIndex(0);
        if (cmbKamar.getItemCount() > 0) cmbKamar.setSelectedIndex(0);
        Calendar cal = Calendar.getInstance();
        spnrCheckin.setValue(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        spnrCheckout.setValue(cal.getTime());
        spnrJumlahTamu.setValue(1);
        cmbStatusReservasi.setSelectedIndex(0);
    }

    public void addSimpanListener(ActionListener l) { btnSimpan.addActionListener(l); }
    public void addUpdateListener(ActionListener l) { btnUpdate.addActionListener(l); }
    public void addHapusListener(ActionListener l) { btnHapus.addActionListener(l); }
    public void addResetListener(ActionListener l) { btnReset.addActionListener(l); }
    public void addCheckInListener(ActionListener l) { btnCheckIn.addActionListener(l); }
    public void addCheckOutListener(ActionListener l) { btnCheckOut.addActionListener(l); }
    public void addCariListener(ActionListener l) { btnCari.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { btnRefresh.addActionListener(l); }
    public void addKalkulasiTrigger(ActionListener l) {
        cmbKamar.addActionListener(l);
    }
    public void addDateChangeTrigger(javax.swing.event.ChangeListener l) {
        spnrCheckin.addChangeListener(l);
        spnrCheckout.addChangeListener(l);
    }
    public void addTableMouseListener(MouseAdapter m) { tblReservasi.addMouseListener(m); }
}
