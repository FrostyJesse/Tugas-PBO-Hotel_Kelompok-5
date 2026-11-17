package controller;

import dao.UserDAO;
import model.User;
import view.LoginView;
import view.MainDashboardView;

import javax.swing.JOptionPane;
import java.sql.SQLException;

/**
 * Controller untuk menangani alur autentikasi Login.
 * Menghubungkan LoginView dengan UserDAO.
 */
public class LoginController {
    private LoginView view;
    private UserDAO userDAO;

    public LoginController(LoginView view, UserDAO userDAO) {
        this.view = view;
        this.userDAO = userDAO;
        initController();
    }

    private void initController() {
        view.addLoginListener(e -> performLogin());
        view.addResetListener(e -> view.clearFields());
    }

    private void performLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Username dan password wajib diisi!", 
                "Validasi Input", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = userDAO.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(view, 
                    "Selamat datang, " + user.getUsername() + " (" + user.getRole() + ")!", 
                    "Login Berhasil", 
                    JOptionPane.INFORMATION_MESSAGE);

                view.dispose(); // Tutup form login

                // Buka MainDashboardView
                MainDashboardView dashboardView = new MainDashboardView(user);
                new DashboardController(dashboardView);
                dashboardView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, 
                    "Username atau password salah.", 
                    "Gagal Login", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, 
                "Gagal terhubung ke database:\n" + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
