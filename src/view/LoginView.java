package view;

import util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

/**
 * View untuk halaman Login Petugas / Resepsionis Hotel.
 * Menggunakan Layout Manager GridBagLayout & BorderLayout tanpa absolute coordinates.
 */
public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JButton btnLogin;
    private JButton btnReset;

    public LoginView() {
        setTitle("Login - Hotel Reservation System");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 15));
        rootPanel.setBackground(UIUtils.COLOR_BG);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 20, 25));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.COLOR_BG);

        JLabel lblTitle = new JLabel("HOTEL RESERVATION SYSTEM", JLabel.CENTER);
        lblTitle.setFont(UIUtils.FONT_HEADER);
        lblTitle.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        JLabel lblSubtitle = new JLabel("Silakan masuk menggunakan akun petugas", JLabel.CENTER);
        lblSubtitle.setFont(UIUtils.FONT_REGULAR);
        lblSubtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.SOUTH);

        // Form Card Panel
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(UIUtils.FONT_BOLD);
        formCard.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsername = new JTextField(15);
        txtUsername.setFont(UIUtils.FONT_REGULAR);
        formCard.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(UIUtils.FONT_BOLD);
        formCard.add(lblPass, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(UIUtils.FONT_REGULAR);
        formCard.add(txtPassword, gbc);

        // Show Password CheckBox
        gbc.gridx = 1;
        gbc.gridy = 2;
        chkShowPassword = new JCheckBox("Tampilkan Password");
        chkShowPassword.setFont(UIUtils.FONT_REGULAR);
        chkShowPassword.setBackground(UIUtils.COLOR_CARD_BG);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
        formCard.add(chkShowPassword, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIUtils.COLOR_BG);

        btnReset = new JButton("Reset");
        UIUtils.styleButton(btnReset, Color.GRAY, Color.WHITE);

        btnLogin = new JButton("Login Masuk");
        UIUtils.styleButton(btnLogin, UIUtils.COLOR_PRIMARY, Color.WHITE);

        buttonPanel.add(btnReset);
        buttonPanel.add(btnLogin);

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(formCard, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(rootPanel);
        getRootPane().setDefaultButton(btnLogin); // Tekan Enter untuk login langsung
    }

    // Getter untuk Controller
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        btnReset.addActionListener(listener);
    }
}
