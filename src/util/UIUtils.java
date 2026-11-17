package util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Utility class untuk styling antarmuka Java Swing agar terlihat
 * modern, profesional, dan bersih.
 */
public class UIUtils {
    // Palet Warna Utama
    public static final Color COLOR_PRIMARY = new Color(41, 128, 185);    // Biru Elegan (#2980B9)
    public static final Color COLOR_PRIMARY_DARK = new Color(31, 78, 120);// Biru Gelap
    public static final Color COLOR_NAVY = new Color(34, 47, 62);         // Dark Navy Sidebar
    public static final Color COLOR_NAVY_LIGHT = new Color(44, 62, 80);   // Navy Hover
    public static final Color COLOR_SUCCESS = new Color(39, 174, 96);     // Hijau (#27AE60)
    public static final Color COLOR_DANGER = new Color(231, 76, 60);      // Merah (#E74C3C)
    public static final Color COLOR_WARNING = new Color(230, 126, 34);    // Orange (#E67E22)
    public static final Color COLOR_BG = new Color(245, 247, 250);        // Abu-abu Terang Canvas
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    public static final Color COLOR_TEXT_MUTED = new Color(127, 140, 141);

    // Font Standar
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_METRIC = new Font("Segoe UI", Font.BOLD, 22);

    /**
     * Mengatur tombol dengan warna dan style yang modern.
     */
    public static void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(7, 14, 7, 14));
    }

    /**
     * Mengatur JTable agar memiliki header modern, row height proporsional,
     * dan alignment yang rapi.
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(FONT_REGULAR);
        table.setSelectionBackground(new Color(214, 234, 248));
        table.setSelectionForeground(COLOR_TEXT_DARK);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(COLOR_TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(Double.class, centerRenderer);
    }

    /**
     * Membuat panel kartu (Card) dengan border lembut dan background putih.
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return card;
    }
}
