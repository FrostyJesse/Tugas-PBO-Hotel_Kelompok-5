package controller;

import dao.TamuDAO;
import exception.DataValidationException;
import model.Tamu;
import view.TamuView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller untuk mengelola interaksi antara TamuView dan TamuDAO (CRUD + Search).
 */
public class TamuController {
    private TamuView view;
    private TamuDAO tamuDAO;

    public TamuController(TamuView view, TamuDAO tamuDAO) {
        this.view = view;
        this.tamuDAO = tamuDAO;
        initController();
        loadAllData();
    }

    private void initController() {
        view.addTambahListener(e -> view.clearForm());
        view.addSimpanListener(e -> simpanTamu());
        view.addUpdateListener(e -> updateTamu());
        view.addHapusListener(e -> hapusTamu());
        view.addResetListener(e -> view.clearForm());
        view.addCariListener(e -> cariTamu());
        view.addRefreshListener(e -> loadAllData());

        // Klik baris tabel untuk memuat ke form
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    DefaultTableModel model = view.getTableModel();
                    view.setIdTamu(model.getValueAt(selectedRow, 0).toString());
                    view.setNama(model.getValueAt(selectedRow, 1).toString());
                    view.setNomorIdentitas(model.getValueAt(selectedRow, 2).toString());
                    view.setNoTelepon(model.getValueAt(selectedRow, 3).toString());
                    view.setEmail(model.getValueAt(selectedRow, 4).toString());
                    view.setAlamat(model.getValueAt(selectedRow, 5) != null ? model.getValueAt(selectedRow, 5).toString() : "");
                }
            }
        });
    }

    public void loadAllData() {
        try {
            List<Tamu> list = tamuDAO.getAllTamu();
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data tamu: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanKeTabel(List<Tamu> list) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        for (Tamu t : list) {
            model.addRow(new Object[]{
                t.getIdTamu(),
                t.getNama(),
                t.getNomorIdentitas(),
                t.getNoTelepon(),
                t.getEmail(),
                t.getAlamat()
            });
        }
    }

    private void validasiInput() throws DataValidationException {
        if (view.getNama().isEmpty()) {
            throw new DataValidationException("Nama tamu tidak boleh kosong!");
        }
        if (view.getNomorIdentitas().isEmpty()) {
            throw new DataValidationException("Nomor identitas (KTP/Paspor) tidak boleh kosong!");
        }
        if (view.getNoTelepon().isEmpty()) {
            throw new DataValidationException("Nomor telepon tidak boleh kosong!");
        }
        if (view.getEmail().isEmpty()) {
            throw new DataValidationException("Email tidak boleh kosong!");
        }
        if (!view.getEmail().contains("@")) {
            throw new DataValidationException("Format email tidak valid (harus mengandung '@')!");
        }
    }

    private void simpanTamu() {
        try {
            validasiInput();
            Tamu tamu = new Tamu(
                view.getNama(),
                view.getNomorIdentitas(),
                view.getNoTelepon(),
                view.getEmail(),
                view.getAlamat()
            );

            tamuDAO.tambahTamu(tamu);
            JOptionPane.showMessageDialog(view, "Data tamu berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadAllData();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan tamu ke database:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTamu() {
        if (view.getIdTamu().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data tamu yang ingin diupdate dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            validasiInput();
            int id = Integer.parseInt(view.getIdTamu());
            Tamu tamu = new Tamu(
                id,
                view.getNama(),
                view.getNomorIdentitas(),
                view.getNoTelepon(),
                view.getEmail(),
                view.getAlamat()
            );

            tamuDAO.updateTamu(tamu);
            JOptionPane.showMessageDialog(view, "Data tamu berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadAllData();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengupdate tamu:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusTamu() {
        if (view.getIdTamu().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data tamu yang ingin dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            view,
            "Apakah Anda yakin ingin menghapus data tamu ini?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(view.getIdTamu());
                tamuDAO.deleteTamu(id);
                JOptionPane.showMessageDialog(view, "Data tamu berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadAllData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus data tamu:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cariTamu() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllData();
            return;
        }
        try {
            List<Tamu> list = tamuDAO.searchTamu(keyword);
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mencari data tamu:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
