package view;

import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * View Panel untuk Manajemen Data Tamu (CRUD + Search).
 * Menampilkan Form Input dan JTable.
 */
public class TamuView extends JPanel {
    private static final long serialVersionUID = 1L;

    // Komponen Form Input
    private JTextField txtIdTamu;
    private JTextField txtNama;
    private JTextField txtNomorIdentitas;
    private JTextField txtNoTelepon;
    private JTextField txtEmail;
    private JTextField txtAlamat;
    private JCheckBox chkMemberVIP;

    // Tombol Aksi
    private JButton btnTambah;
    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnReset;

    // Pencarian
    private JTextField txtCari;
    private JButton btnCari;
    private JButton btnRefresh;

    // Tabel
    private JTable tblTamu;
    private DefaultTableModel tableModel;

    public TamuView() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
    }

    private void initComponents() {
        // 1. Header Judul
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BG);
        JLabel lblTitle = new JLabel("MANAJEMEN DATA TAMU HOTEL");
        lblTitle.setFont(UIUtils.FONT_HEADER);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // 2. Panel Kiri: Form Input Data Tamu
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdTamu = new JTextField();
        txtIdTamu.setEditable(false);
        txtIdTamu.setVisible(false); // ID hidden untuk binding

        txtNama = new JTextField(15);
        txtNomorIdentitas = new JTextField(15);
        txtNoTelepon = new JTextField(15);
        txtEmail = new JTextField(15);
        txtAlamat = new JTextField(15);
        chkMemberVIP = new JCheckBox("Tamu Berstatus Loyal / VIP Member");
        chkMemberVIP.setBackground(UIUtils.COLOR_CARD_BG);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formCard.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtNama, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("No. Identitas (KTP):"), gbc);
        gbc.gridx = 1;
        formCard.add(txtNomorIdentitas, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("No. Telepon / WA:"), gbc);
        gbc.gridx = 1;
        formCard.add(txtNoTelepon, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Alamat Email:"), gbc);
        gbc.gridx = 1;
        formCard.add(txtEmail, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Alamat Rumah:"), gbc);
        gbc.gridx = 1;
        formCard.add(txtAlamat, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formCard.add(chkMemberVIP, gbc);

        // Panel Tombol Form (Tambah, Simpan, Update, Hapus, Reset)
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

        // 3. Panel Kanan: Search Bar + JTable
        JPanel tableCard = UIUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setBackground(UIUtils.COLOR_CARD_BG);
        searchPanel.add(new JLabel("Cari Tamu:"));
        txtCari = new JTextField(18);
        searchPanel.add(txtCari);

        btnCari = new JButton("Cari");
        UIUtils.styleButton(btnCari, UIUtils.COLOR_PRIMARY, Color.WHITE);
        searchPanel.add(btnCari);

        btnRefresh = new JButton("Refresh / Tampilkan Semua");
        UIUtils.styleButton(btnRefresh, Color.GRAY, Color.WHITE);
        searchPanel.add(btnRefresh);

        // JTable Tamu
        String[] columns = {"ID", "Nama Lengkap", "No. Identitas", "No. Telepon", "Email", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only di grid
            }
        };

        tblTamu = new JTable(tableModel);
        UIUtils.styleTable(tblTamu);
        JScrollPane scrollPane = new JScrollPane(tblTamu);

        tableCard.add(searchPanel, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Gabungkan ke Layout Utama
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(UIUtils.COLOR_BG);
        contentPanel.add(formCard, BorderLayout.WEST);
        contentPanel.add(tableCard, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS & DATA BINDING
    // ==========================================

    public String getIdTamu() { return txtIdTamu.getText().trim(); }
    public void setIdTamu(String id) { txtIdTamu.setText(id); }

    public String getNama() { return txtNama.getText().trim(); }
    public void setNama(String nama) { txtNama.setText(nama); }

    public String getNomorIdentitas() { return txtNomorIdentitas.getText().trim(); }
    public void setNomorIdentitas(String no) { txtNomorIdentitas.setText(no); }

    public String getNoTelepon() { return txtNoTelepon.getText().trim(); }
    public void setNoTelepon(String telp) { txtNoTelepon.setText(telp); }

    public String getEmail() { return txtEmail.getText().trim(); }
    public void setEmail(String email) { txtEmail.setText(email); }

    public String getAlamat() { return txtAlamat.getText().trim(); }
    public void setAlamat(String alamat) { txtAlamat.setText(alamat); }

    public boolean isMemberVIP() { return chkMemberVIP.isSelected(); }
    public void setMemberVIP(boolean v) { chkMemberVIP.setSelected(v); }

    public String getSearchKeyword() { return txtCari.getText().trim(); }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return tblTamu; }

    public void clearForm() {
        txtIdTamu.setText("");
        txtNama.setText("");
        txtNomorIdentitas.setText("");
        txtNoTelepon.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
        chkMemberVIP.setSelected(false);
        txtNama.requestFocus();
    }

    // Event Listener Registrations
    public void addTambahListener(ActionListener l) { btnTambah.addActionListener(l); }
    public void addSimpanListener(ActionListener l) { btnSimpan.addActionListener(l); }
    public void addUpdateListener(ActionListener l) { btnUpdate.addActionListener(l); }
    public void addHapusListener(ActionListener l) { btnHapus.addActionListener(l); }
    public void addResetListener(ActionListener l) { btnReset.addActionListener(l); }
    public void addCariListener(ActionListener l) { btnCari.addActionListener(l); }
    public void addRefreshListener(ActionListener l) { btnRefresh.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter m) { tblTamu.addMouseListener(m); }
}
