package controller;

import dao.PembayaranDAO;
import dao.ReservasiDAO;
import exception.DataValidationException;
import model.Pembayaran;
import model.Reservasi;
import view.PembayaranView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller untuk mengelola transaksi Pembayaran hotel dan cetak kwitansi.
 */
public class PembayaranController {
    private PembayaranView view;
    private PembayaranDAO pembayaranDAO;
    private ReservasiDAO reservasiDAO;

    private static final DateTimeFormatter DTO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PembayaranController(PembayaranView view, PembayaranDAO pembayaranDAO, ReservasiDAO reservasiDAO) {
        this.view = view;
        this.pembayaranDAO = pembayaranDAO;
        this.reservasiDAO = reservasiDAO;

        initController();
        loadComboReservasi();
        loadAllData();
    }

    private void initController() {
        view.addBayarListener(e -> prosesBayar());
        view.addStrukListener(e -> cetakKwitansi());
        view.addHapusListener(e -> hapusPembayaran());
        view.addResetListener(e -> view.clearForm());
        view.addCariListener(e -> cariPembayaran());
        view.addRefreshListener(e -> {
            loadComboReservasi();
            loadAllData();
        });

        // Auto update tagihan saat ganti pilihan reservasi
        view.addReservasiSelectListener(e -> updateTagihanInfo());

        // Klik tabel pembayaran
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    try {
                        int idBayar = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
                        Pembayaran p = pembayaranDAO.getPembayaranById(idBayar);
                        if (p != null) {
                            view.setIdPembayaran(String.valueOf(p.getIdPembayaran()));
                            selectReservasiById(p.getIdReservasi());
                            view.setJumlahBayar(String.valueOf(p.getJumlahBayar()));
                            view.setMetodePembayaran(p.getMetodePembayaran());
                            view.setStatusPembayaran(p.getStatusPembayaran());
                        }
                    } catch (Exception ex) {
                        System.err.println("Gagal memilih data pembayaran: " + ex.getMessage());
                    }
                }
            }
        });
    }

    public void loadComboReservasi() {
        try {
            view.getCmbReservasi().removeAllItems();
            List<Reservasi> list = reservasiDAO.getAllReservasi();
            for (Reservasi r : list) {
                view.getCmbReservasi().addItem(r);
            }
            updateTagihanInfo();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat daftar reservasi:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTagihanInfo() {
        Reservasi r = view.getSelectedReservasi();
        if (r != null) {
            view.setTagihan(r.getTotalHarga());
            if (view.getJumlahBayar().isEmpty()) {
                view.setJumlahBayar(String.format("%.0f", r.getTotalHarga()));
            }
        }
    }

    public void loadAllData() {
        try {
            List<Pembayaran> list = pembayaranDAO.getAllPembayaran();
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data pembayaran:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanKeTabel(List<Pembayaran> list) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        for (Pembayaran p : list) {
            model.addRow(new Object[]{
                p.getIdPembayaran(),
                p.getIdReservasi(),
                p.getTanggalPembayaran().format(DTO_FORMATTER),
                String.format("%,.0f", p.getJumlahBayar()),
                p.getMetodePembayaran(),
                p.getStatusPembayaran()
            });
        }
    }

    private void selectReservasiById(int idReservasi) {
        for (int i = 0; i < view.getCmbReservasi().getItemCount(); i++) {
            Reservasi r = view.getCmbReservasi().getItemAt(i);
            if (r.getIdReservasi() == idReservasi) {
                view.getCmbReservasi().setSelectedIndex(i);
                break;
            }
        }
    }

    private void prosesBayar() {
        Reservasi r = view.getSelectedReservasi();
        if (r == null) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi yang akan dibayar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String bayarStr = view.getJumlahBayar();
            if (bayarStr.isEmpty()) {
                throw new DataValidationException("Jumlah pembayaran wajib diisi!");
            }
            double jumlahBayar;
            try {
                jumlahBayar = Double.parseDouble(bayarStr);
            } catch (NumberFormatException e) {
                throw new DataValidationException("Jumlah pembayaran harus berupa angka nominal!");
            }
            if (jumlahBayar < 0) {
                throw new DataValidationException("Jumlah pembayaran tidak boleh negatif!");
            }

            Pembayaran p = new Pembayaran(
                r.getIdReservasi(),
                jumlahBayar,
                view.getMetodePembayaran(),
                view.getStatusPembayaran()
            );

            pembayaranDAO.tambahPembayaran(p);

            JOptionPane.showMessageDialog(view, 
                "Pembayaran sebesar Rp " + String.format("%,.0f", jumlahBayar) + " berhasil diproses!\n" +
                "Metode: " + p.getMetodePembayaran() + "\n" +
                "Status: " + p.getStatusPembayaran(), 
                "Pembayaran Sukses", 
                JOptionPane.INFORMATION_MESSAGE);

            view.clearForm();
            loadAllData();
        } catch (DataValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Peringatan Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal memproses pembayaran:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cetakKwitansi() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Silakan pilih salah satu baris pembayaran dari tabel untuk mencetak kwitansi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idBayar = Integer.parseInt(view.getTableModel().getValueAt(row, 0).toString());
            Pembayaran p = pembayaranDAO.getPembayaranById(idBayar);
            if (p != null) {
                Reservasi r = reservasiDAO.getReservasiById(p.getIdReservasi());

                StringBuilder sb = new StringBuilder();
                sb.append("=====================================================\n");
                sb.append("            KWITANSI PEMBAYARAN RESMI                \n");
                sb.append("          HOTEL RESERVATION SYSTEM                   \n");
                sb.append("=====================================================\n");
                sb.append("No. Kwitansi      : PAY-").append(p.getIdPembayaran()).append("\n");
                sb.append("Waktu Transaksi   : ").append(p.getTanggalPembayaran().format(DTO_FORMATTER)).append("\n");
                sb.append("ID Reservasi      : #").append(p.getIdReservasi()).append("\n");
                if (r != null) {
                    sb.append("Nama Tamu         : ").append(r.getTamu() != null ? r.getTamu().getNama() : "-").append("\n");
                    sb.append("Nomor Kamar       : ").append(r.getKamar() != null ? r.getKamar().getNomorKamar() : "-").append("\n");
                    sb.append("Tipe Kamar        : ").append(r.getKamar() != null ? r.getKamar().getTipeKamar() : "-").append("\n");
                    sb.append("Total Tagihan     : Rp ").append(String.format("%,.0f", r.getTotalHarga())).append("\n");
                }
                sb.append("-----------------------------------------------------\n");
                sb.append("Jumlah Dibayarkan : Rp ").append(String.format("%,.0f", p.getJumlahBayar())).append("\n");
                sb.append("Metode Pembayaran : ").append(p.getMetodePembayaran()).append("\n");
                sb.append("Status Pelunasan  : ").append(p.getStatusPembayaran().toUpperCase()).append("\n");
                sb.append("=====================================================\n");
                sb.append("       Terima kasih telah mempercayai hotel kami!    \n");

                JOptionPane.showMessageDialog(view, sb.toString(), "Kwitansi Pembayaran #" + p.getIdPembayaran(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mengambil data kwitansi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusPembayaran() {
        if (view.getIdPembayaran().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data pembayaran yang ingin dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            view,
            "Apakah Anda yakin ingin menghapus catatan pembayaran ini?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(view.getIdPembayaran());
                pembayaranDAO.deletePembayaran(id);
                JOptionPane.showMessageDialog(view, "Data pembayaran berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadAllData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Gagal menghapus pembayaran:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cariPembayaran() {
        String keyword = view.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllData();
            return;
        }
        try {
            List<Pembayaran> list = pembayaranDAO.searchPembayaran(keyword);
            tampilkanKeTabel(list);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Gagal mencari pembayaran:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
