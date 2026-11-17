package view;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * View Panel untuk Manajemen Data Kamar (CRUD + Search).
 * Mendukung tipe kamar Standard, Deluxe, Suite, status, dan kapasitas.
 */
public class KamarView extends JPanel {
    private static final long serialVersionUID = 1L;

    // Form Input
    private JTextField txtIdKamar;
    private JTextField txtNomorKamar;
    private JComboBox<String> cmbTipeKamar;
    private JTextField txtHarga;
    private JSpinner spnrKapasitas;
    private JComboBox<String> cmbStatus;

    // Tombol Form
    private JButton btnTambah;
    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnReset;

    // Search Bar
    private JTextField txtCari;
    private JButton btnCari;
    private JButton btnRefresh;

    // JTable
    private JTable tblKamar;
    private DefaultTableModel tableModel;

    public KamarView() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BG);
        JLabel lblTitle = new JLabel("MANAJEMEN DATA KAMAR HOTEL");
        lblTitle.setFont(UIUtils.FONT_HEADER);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Form Card Panel
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdKamar = new JTextField();
        txtIdKamar.setVisible(false);

        txtNomorKamar = new JTextField(15);
        cmbTipeKamar = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        txtHarga = new JTextField(15);
        spnrKapasitas = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        cmbStatus = new JComboBox<>(new String[]{"Tersedia", "Dipesan", "Terisi", "Maintenance"});

        // Auto isi saran harga saat ganti tipe kamar
        cmbTipeKamar.addActionListener(e -> {
            String selected = (String) cmbTipeKamar.getSelectedItem();
            if (txtHarga.getText().trim().isEmpty() || txtIdKamar.getText().trim().isEmpty()) {
                if ("Standard".equals(selected)) txtHarga.setText("350000");
                else if ("Deluxe".equals(selected)) txtHarga.setText("550000");
                else if ("Suite".equals(selected)) txtHarga.setText("950000");
            }
        });

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formCard.add(new JLabel("Nomor Kamar:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtNomorKamar, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Tipe Kamar:"), gbc);
        gbc.gridx = 1;
        formCard.add(cmbTipeKamar, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Harga per Malam (Rp):"), gbc);
        gbc.gridx = 1;
        formCard.add(txtHarga, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Kapasitas (Orang):"), gbc);
        gbc.gridx = 1;
        formCard.add(spnrKapasitas, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Status Kamar:"), gbc);
        gbc.gridx = 1;
        formCard.add(cmbStatus, gbc);

        // Tombol Form
        row++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel.setBackground(UIUtils.COLOR_CARD_BG);

        btnTambah = new JButton("Tambah");
        UIUtils.styleButton(btnTambah, UIUtils.COLOR_PRIMARY, Color.WHITE);

        btnSimpan = new JButton("Simpan");
        UIUtils.styleButton(btnSimpan, UIUtils.COLOR_SUCCESS, Color.WHITE);

        btnUpdate = new JButton("Update");
        UIUtils.styleButton(btnUpdate, UIUtils.COLOR_WARNING, Color.WHITE);

        btnHapus = new JButton("Hapus");
        UIUtils.styleButton(btnHapus, UIUtils.COLOR_DANGER, Color.WHITE);

        btnReset = new JButton("Reset");
        UIUtils.styleButton(btnReset, Color.GRAY, Color.WHITE);

        btnPanel.add(btnTambah);
        btnPanel.add(btnSimpan);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(btnPanel, gbc);

        // Panel Tabel + Pencarian
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setBackground(UIUtils.COLOR_CARD_BG);
        searchPanel.add(new JLabel("Cari Kamar:"));
        txtCari = new JTextField(18);
        searchPanel.add(txtCari);

        btnCari = new JButton("Cari");
        UIUtils.styleButton(btnCari, UIUtils.COLOR_PRIMARY, Color.WHITE);
        searchPanel.add(btnCari);

        btnRefresh = new JButton("Refresh / Tampilkan Semua");
        UIUtils.styleButton(btnRefresh, Color.GRAY, Color.WHITE);
        searchPanel.add(btnRefresh);

        String[] cols = {"ID", "No. Kamar", "Tipe Kamar", "Harga/Malam", "Kapasitas", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblKamar = new JTable(tableModel);
        UIUtils.styleTable(tblKamar);
        JScrollPane scrollPane = new JScrollPane(tblKamar);

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
    public String getIdKamar() { return txtIdKamar.getText().trim(); }
    public void setIdKamar(String id) { txtIdKamar.setText(id); }

    public String getNomorKamar() { return txtNomorKamar.getText().trim(); }
    public void setNomorKamar(String no) { txtNomorKamar.setText(no); }

    public String getTipeKamar() { return (String) cmbTipeKamar.getSelectedItem(); }
    public void setTipeKamar(String tipe) { cmbTipeKamar.setSelectedItem(tipe); }

    public String getHarga() { return txtHarga.getText().trim(); }
    public void setHarga(String h) { txtHarga.setText(h); }

    public int getKapasitas() { return (Integer) spnrKapasitas.getValue(); }
    public void setKapasitas(int k) { spnrKapasitas.setValue(k); }

    public String getStatus() { return (String) cmbStatus.getSelectedItem(); }
    public void setStatus(String s) { cmbStatus.setSelectedItem(s); }

    public String getSearchKeyword() { return txtCari.getText().trim(); }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return tblKamar; }

    public void clearForm() {
        txtIdKamar.setText("");
        txtNomorKamar.setText("");
        cmbTipeKamar.setSelectedIndex(0);
        txtHarga.setText("350000");
        spnrKapasitas.setValue(2);
        cmbStatus.setSelectedIndex(0);
        txtNomorKamar.requestFocus();
    }

    public void addTambahListener(ActionListener l) { btnTambah.addActionListener(l); }
    public void addSimpanListener(ActionListener l) { btnSimpan.addActionListener(l); }
    public void addUpdateListener(ActionListener l) { btnUpdate.addActionListener(l); }
    public void addHapusListener(ActionListener l) { btnHapus.addActionListener(l); }
    public void addResetListener(ActionListener l) { btnReset.addActionListener(l); }
    public void addCariListener(ActionListener l) { btnCari.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { btnRefresh.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter m) { tblKamar.addMouseListener(m); }
}
