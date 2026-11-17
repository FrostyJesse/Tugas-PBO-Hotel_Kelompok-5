import controller.LoginController;
import dao.UserDAO;
import view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point utama aplikasi Sistem Reservasi Hotel.
 * Menginisialisasi Look and Feel Swing dan membuka form Login.
 */
public class Main {
    public static void main(String[] args) {
        // Mengatur tema Look and Feel sistem operasi agar UI Swing terlihat modern dan rapi
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        // Jalankan aplikasi di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            UserDAO userDAO = new UserDAO();
            new LoginController(loginView, userDAO);
            loginView.setVisible(true);
        });
    }
}
