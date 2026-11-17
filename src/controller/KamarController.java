package controller;

import dao.KamarDAO;
import exception.DataValidationException;
import model.Deluxe;
import model.Kamar;
import model.Standard;
import model.Suite;
import view.KamarView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller untuk mengelola interaksi antara KamarView dan KamarDAO (CRUD + Search).
 * Menerapkan pembuatan objek model polimorfik (Standard, Deluxe, Suite).
 */
public class KamarController {
    private KamarView view;
    private KamarDAO kamarDAO;

    public KamarController(KamarView view, KamarDAO kamarDAO) {
        this.view = view;
        this.kamarDAO = kamarDAO;
        initController();
        loadAllData();
    }

    private void initController() {
        view.addTambahListener(e -> view.clearForm());
        view.addSimpanListener(e -> simpanKamar());
        view.addUpdateListener(e -> updateKamar());
        view.addHapusListener(e -> hapusKamar());
        view.addResetListener(e -> view.clearForm());
        view.addCariListener(e -> cariKamar());
        view.addRefreshListener(e -> loadAllData());

        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = view.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    DefaultTableModel model = view.getTableModel();
                    view.setIdKamar(model.getValueAt(selectedRow, 0).toString());
                    view.setNomorKamar(model.getValueAt(selectedRow, 1).toString());
                    view.setTipeKamar(model.getValueAt(selectedRow, 2).toString());

                    // Format harga numerik (hilangkan karakter Rp/koma jika ada)
                    String hargaStr = model.getValueAt(selectedRow, 3).toString().replaceAll("[^0-9.]", "");
                    view.setHarga(hargaStr);

                    view.setKapasitas(Integer.parseInt(model.getValueAt(selectedRow, 4).toString()));
                    view.setStatus(model.getValueAt(selectedRow, 5).toString());
                }
            }
        });
    }

    public void loadAllData() {
        try {
            List<Kamar> list = kamarDAO.getAllKamar();
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data kamar:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanKeTabel(List<Kamar> list) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        for (Kamar k : list) {
            model.addRow(new Object[]{
                k.getIdKamar(),
                k.getNomorKamar(),
                k.getTipeKamar(),
                String.format("%,.0f", k.getHarga()),
                k.getKapasitas(),
                k.getStatus()
            });
        }
    }

    private double validasiInput() throws DataValidationException {
        if (view.getNomorKamar().isEmpty()) {
            throw new DataValidationException("Nomor kamar tidak boleh kosong!");
        }
        if (view.getHarga().isEmpty()) {
            throw new DataValidationException("Harga kamar tidak boleh kosong!");
        }
        double harga;
        try {
            harga = Double.parseDouble(view.getHarga());
        } catch (NumberFormatException e) {
            throw new DataValidationException("Harga kamar harus berupa angka yang valid!");
        }
        if (harga < 0) {
            throw new DataValidationException("Harga kamar tidak boleh bernilai negatif!");
        }
        if (view.getKapasitas() < 1) {
            throw new DataValidationException("Kapasitas kamar minimal 1 orang!");
        }
        return harga;
    }

    /**
     * Membentuk instance Kamar turunan secara Polimorfik.
     */
    private Kamar createPolymorphicKamar(int id, String no, String tipe, double harga, String status, int kapasitas) {
        if ("Deluxe".equalsIgnoreCase(tipe)) {
            return (id > 0) ? new Deluxe(id, no, harga, status, kapasitas) : new Deluxe(no, harga, status, kapasitas);
        } else if ("Suite".equalsIgnoreCase(tipe)) {
            return (id > 0) ? new Suite(id, no, harga, status, kapasitas) : new Suite(no, harga, status, kapasitas);
        } else {
            return (id > 0) ? new Standard(id, no, harga, status, kapasitas) : new Standard(no, harga, status, kapasitas);
        }
    }

    private void simpanKamar() {
        try {
            double harga = validasiInput();
            Kamar kamar = createPolymorphicKamar(
                0,
                view.getNomorKamar(),
                view.getTipeKamar(),
                harga,
                view.getStatus(),
                view.getKapasitas()
            );

            kamarDAO.tambahKamar(kamar);
            JOptionPane.showMessageDialog(view, "Data kamar berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadAllData();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan kamar ke database:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKamar() {
        if (view.getIdKamar().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data kamar yang ingin diupdate dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double harga = validasiInput();
            int id = Integer.parseInt(view.getIdKamar());
            Kamar kamar = createPolymorphicKamar(
                id,
                view.getNomorKamar(),
                view.getTipeKamar(),
                harga,
                view.getStatus(),
                view.getKapasitas()
            );

            kamarDAO.updateKamar(kamar);
            JOptionPane.showMessageDialog(view, "Data kamar berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadAllData();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengupdate kamar:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKamar() {
        if (view.getIdKamar().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data kamar yang ingin dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            view,
            "Apakah Anda yakin ingin menghapus data kamar ini?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(view.getIdKamar());
                kamarDAO.deleteKamar(id);
                JOptionPane.showMessageDialog(view, "Data kamar berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadAllData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus kamar. Kamar mungkin sedang terkait dengan data reservasi aktif:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cariKamar() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllData();
            return;
        }
        try {
            List<Kamar> list = kamarDAO.searchKamar(keyword);
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mencari data kamar:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
