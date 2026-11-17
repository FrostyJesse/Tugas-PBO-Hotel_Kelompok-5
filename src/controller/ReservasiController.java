package controller;

import dao.KamarDAO;
import dao.ReservasiDAO;
import dao.TamuDAO;
import exception.DataValidationException;
import exception.ReservasiException;
import model.CheckIn;
import model.CheckOut;
import model.Diskon;
import model.DiskonDurasi;
import model.Kamar;
import model.Reservasi;
import model.Tamu;
import view.ReservasiView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller untuk mengelola transaksi Reservasi, perhitungan tarif polimorfik,
 * serta alur operasional Check-In dan Check-Out.
 */
public class ReservasiController {
    private ReservasiView view;
    private ReservasiDAO reservasiDAO;
    private TamuDAO tamuDAO;
    private KamarDAO kamarDAO;

    public ReservasiController(ReservasiView view, ReservasiDAO reservasiDAO, TamuDAO tamuDAO, KamarDAO kamarDAO) {
        this.view = view;
        this.reservasiDAO = reservasiDAO;
        this.tamuDAO = tamuDAO;
        this.kamarDAO = kamarDAO;

        initController();
        loadComboData();
        loadAllData();
        hitungKalkulasiTarif();
    }

    private void initController() {
        view.addSimpanListener(e -> simpanReservasi());
        view.addUpdateListener(e -> updateReservasi());
        view.addHapusListener(e -> hapusReservasi());
        view.addResetListener(e -> {
            view.clearForm();
            hitungKalkulasiTarif();
        });
        view.addCheckInListener(e -> prosesCheckIn());
        view.addCheckOutListener(e -> prosesCheckOut());
        view.addCariListener(e -> cariReservasi());
        view.addRefreshListener(e -> {
            loadComboData();
            loadAllData();
        });

        // Trigger kalkulasi otomatis saat kamar atau tanggal berubah
        view.addKalkulasiTrigger(e -> hitungKalkulasiTarif());
        view.addDateChangeTrigger(e -> hitungKalkulasiTarif());

        // Klik baris tabel
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int id = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
                        Reservasi r = reservasiDAO.getReservasiById(id);
                        if (r != null) {
                            view.setIdReservasi(String.valueOf(r.getIdReservasi()));
                            selectTamuById(r.getIdTamu());
                            selectKamarById(r.getIdKamar());
                            view.setCheckinDate(r.getTanggalCheckin());
                            view.setCheckoutDate(r.getTanggalCheckout());
                            view.setJumlahTamu(r.getJumlahTamu());
                            view.setStatusReservasi(r.getStatusReservasi());
                            hitungKalkulasiTarif();
                        }
                    } catch (Exception ex) {
                        System.err.println("Gagal memilih reservasi: " + ex.getMessage());
                    }
                }
            }
        });
    }

    public void loadComboData() {
        try {
            // Muat Tamu ke ComboBox
            view.getCmbTamu().removeAllItems();
            List<Tamu> tamuList = tamuDAO.getAllTamu();
            for (Tamu t : tamuList) {
                view.getCmbTamu().addItem(t);
            }

            // Muat Kamar ke ComboBox
            view.getCmbKamar().removeAllItems();
            List<Kamar> kamarList = kamarDAO.getAllKamar();
            for (Kamar k : kamarList) {
                view.getCmbKamar().addItem(k);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat opsi Tamu/Kamar:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadAllData() {
        try {
            List<Reservasi> list = reservasiDAO.getAllReservasi();
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanKeTabel(List<Reservasi> list) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        for (Reservasi r : list) {
            String namaTamu = (r.getTamu() != null) ? r.getTamu().getNama() : ("Tamu #" + r.getIdTamu());
            String noKamar = (r.getKamar() != null) ? r.getKamar().getNomorKamar() : ("Kamar #" + r.getIdKamar());
            String tipeKamar = (r.getKamar() != null) ? r.getKamar().getTipeKamar() : "-";
            int malam = r.hitungJumlahMalam();

            model.addRow(new Object[]{
                r.getIdReservasi(),
                namaTamu,
                noKamar,
                tipeKamar,
                r.getTanggalCheckin(),
                r.getTanggalCheckout(),
                malam,
                r.getJumlahTamu(),
                r.getStatusReservasi(),
                String.format("%,.0f", r.getTotalHarga())
            });
        }
    }

    private void selectTamuById(int idTamu) {
        for (int i = 0; i < view.getCmbTamu().getItemCount(); i++) {
            Tamu t = view.getCmbTamu().getItemAt(i);
            if (t.getIdTamu() == idTamu) {
                view.getCmbTamu().setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectKamarById(int idKamar) {
        for (int i = 0; i < view.getCmbKamar().getItemCount(); i++) {
            Kamar k = view.getCmbKamar().getItemAt(i);
            if (k.getIdKamar() == idKamar) {
                view.getCmbKamar().setSelectedIndex(i);
                break;
            }
        }
    }

    private void hitungKalkulasiTarif() {
        Kamar k = view.getSelectedKamar();
        if (k == null) return;

        LocalDate checkin = view.getCheckinDate();
        LocalDate checkout = view.getCheckoutDate();

        long diff = ChronoUnit.DAYS.between(checkin, checkout);
        int malam = (diff <= 0) ? 1 : (int) diff;

        // Polymorphic calculation (Standard, Deluxe, Suite)
        double subtotal = k.hitungTarif(malam);

        // Diskon interface strategy
        Diskon diskonDurasi = new DiskonDurasi(malam);
        double potongan = diskonDurasi.hitungDiskon(subtotal);
        double totalAkhir = subtotal - potongan;

        view.updateKalkulasiLabel(k.getTipeKamar(), malam, subtotal, potongan, totalAkhir);
    }

    private void validasiInput(Tamu t, Kamar k, LocalDate checkin, LocalDate checkout, int jmlTamu) throws DataValidationException {
        if (t == null) throw new DataValidationException("Silakan pilih tamu terlebih dahulu!");
        if (k == null) throw new DataValidationException("Silakan pilih kamar terlebih dahulu!");
        if (!checkout.isAfter(checkin)) {
            throw new DataValidationException("Gagal membuat reservasi:\nTanggal check-out harus lebih besar dari tanggal check-in.");
        }
        if (jmlTamu < 1) {
            throw new DataValidationException("Jumlah tamu minimal 1 orang!");
        }
        if (jmlTamu > k.getKapasitas()) {
            throw new DataValidationException("Jumlah tamu (" + jmlTamu + ") melebihi kapasitas kamar (" + k.getKapasitas() + " orang)!");
        }
    }

    private void simpanReservasi() {
        try {
            Tamu t = view.getSelectedTamu();
            Kamar k = view.getSelectedKamar();
            LocalDate in = view.getCheckinDate();
            LocalDate out = view.getCheckoutDate();
            int jmlTamu = view.getJumlahTamu();

            validasiInput(t, k, in, out, jmlTamu);

            if ("Maintenance".equalsIgnoreCase(k.getStatus())) {
                throw new DataValidationException("Kamar " + k.getNomorKamar() + " sedang dalam pemeliharaan (Maintenance)!");
            }

            int malam = (int) ChronoUnit.DAYS.between(in, out);
            double subtotal = k.hitungTarif(malam);
            Diskon diskon = new DiskonDurasi(malam);
            double potongan = diskon.hitungDiskon(subtotal);
            double total = subtotal - potongan;

            Reservasi r = new Reservasi(t.getIdTamu(), k.getIdKamar(), in, out, jmlTamu, view.getStatusReservasi(), total);
            reservasiDAO.tambahReservasi(r);

            // Update status kamar jadi Dipesan jika reservasi baru
            if ("Tersedia".equalsIgnoreCase(k.getStatus())) {
                kamarDAO.updateStatusKamar(k.getIdKamar(), "Dipesan");
            }

            JOptionPane.showMessageDialog(view, "Reservasi baru berhasil dibuat!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadComboData();
            loadAllData();
            hitungKalkulasiTarif();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReservasi() {
        if (view.getIdReservasi().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi yang ingin diupdate dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(view.getIdReservasi());
            Tamu t = view.getSelectedTamu();
            Kamar k = view.getSelectedKamar();
            LocalDate in = view.getCheckinDate();
            LocalDate out = view.getCheckoutDate();
            int jmlTamu = view.getJumlahTamu();

            validasiInput(t, k, in, out, jmlTamu);

            int malam = (int) ChronoUnit.DAYS.between(in, out);
            double subtotal = k.hitungTarif(malam);
            Diskon diskon = new DiskonDurasi(malam);
            double total = subtotal - diskon.hitungDiskon(subtotal);

            Reservasi r = new Reservasi(id, t.getIdTamu(), k.getIdKamar(), in, out, jmlTamu, view.getStatusReservasi(), total);
            reservasiDAO.updateReservasi(r);

            JOptionPane.showMessageDialog(view, "Data reservasi berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm();
            loadAllData();
            hitungKalkulasiTarif();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengupdate reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusReservasi() {
        if (view.getIdReservasi().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi yang ingin dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            view,
            "Apakah Anda yakin ingin menghapus data reservasi ini?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(view.getIdReservasi());
                reservasiDAO.deleteReservasi(id);
                JOptionPane.showMessageDialog(view, "Data reservasi berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadComboData();
                loadAllData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void prosesCheckIn() {
        if (view.getIdReservasi().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi dari tabel untuk melakukan Check-In!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(view.getIdReservasi());
            Reservasi r = reservasiDAO.getReservasiById(id);
            if (r == null) {
                throw new ReservasiException("Reservasi #" + id + " tidak ditemukan!");
            }

            CheckIn checkIn = new CheckIn(r);
            checkIn.prosesCheckIn();

            // Simpan perubahan ke database
            reservasiDAO.updateStatusReservasi(r.getIdReservasi(), "CHECKED_IN");
            if (r.getKamar() != null) {
                kamarDAO.updateStatusKamar(r.getKamar().getIdKamar(), "Terisi");
            }

            JOptionPane.showMessageDialog(
                view,
                "Check-In BERHASIL!\n\n" +
                "Tamu: " + (r.getTamu() != null ? r.getTamu().getNama() : "-") + "\n" +
                "Kamar: " + (r.getKamar() != null ? r.getKamar().getNomorKamar() : "-") + " (Status Baru: Terisi)\n" +
                "Status Reservasi: CHECKED_IN",
                "Check-In Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );

            loadComboData();
            loadAllData();
        } catch (ReservasiException ex) {
            JOptionPane.showMessageDialog(view, "Gagal Check-In:\n" + ex.getMessage(), "Operasional Ditolak", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error Database saat Check-In:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesCheckOut() {
        if (view.getIdReservasi().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi dari tabel untuk melakukan Check-Out!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(view.getIdReservasi());
            Reservasi r = reservasiDAO.getReservasiById(id);
            if (r == null) {
                throw new ReservasiException("Reservasi #" + id + " tidak ditemukan!");
            }

            CheckOut checkOut = new CheckOut(r);
            checkOut.prosesCheckOut();

            // Simpan perubahan ke database
            reservasiDAO.updateStatusReservasi(r.getIdReservasi(), "CHECKED_OUT");
            if (r.getKamar() != null) {
                kamarDAO.updateStatusKamar(r.getKamar().getIdKamar(), "Tersedia");
            }

            // Tampilkan Ringkasan Tagihan Check-Out Lengkap
            JOptionPane.showMessageDialog(
                view,
                checkOut.getRingkasanTagihan(),
                "Ringkasan Check-Out & Tagihan",
                JOptionPane.INFORMATION_MESSAGE
            );

            loadComboData();
            loadAllData();
        } catch (ReservasiException ex) {
            JOptionPane.showMessageDialog(view, "Gagal Check-Out:\n" + ex.getMessage(), "Operasional Ditolak", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error Database saat Check-Out:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariReservasi() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllData();
            return;
        }
        try {
            List<Reservasi> list = reservasiDAO.searchReservasi(keyword);
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mencari data reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
