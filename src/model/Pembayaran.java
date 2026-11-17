package model;

import java.time.LocalDateTime;

/**
 * Model Pembayaran merepresentasikan transaksi pembayaran reservasi hotel.
 * Menerapkan pilar Encapsulation dan relasi Asosiasi dengan Reservasi.
 */
public class Pembayaran {
    private int idPembayaran;
    private int idReservasi;
    private Reservasi reservasi;
    private LocalDateTime tanggalPembayaran;
    private double jumlahBayar;
    private String metodePembayaran; // Cash, Transfer, Debit, QRIS
    private String statusPembayaran; // Belum Lunas, DP, Lunas

    public Pembayaran() {
        this.tanggalPembayaran = LocalDateTime.now();
        this.statusPembayaran = "Belum Lunas";
        this.metodePembayaran = "Cash";
    }

    public Pembayaran(int idPembayaran, int idReservasi, LocalDateTime tanggalPembayaran, 
                      double jumlahBayar, String metodePembayaran, String statusPembayaran) {
        setIdPembayaran(idPembayaran);
        setIdReservasi(idReservasi);
        setTanggalPembayaran(tanggalPembayaran);
        setJumlahBayar(jumlahBayar);
        setMetodePembayaran(metodePembayaran);
        setStatusPembayaran(statusPembayaran);
    }

    public Pembayaran(int idReservasi, double jumlahBayar, String metodePembayaran, String statusPembayaran) {
        this(0, idReservasi, LocalDateTime.now(), jumlahBayar, metodePembayaran, statusPembayaran);
    }

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(int idPembayaran) {
        if (idPembayaran < 0) {
            throw new IllegalArgumentException("ID Pembayaran tidak boleh negatif");
        }
        this.idPembayaran = idPembayaran;
    }

    public int getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(int idReservasi) {
        if (idReservasi < 0) {
            throw new IllegalArgumentException("ID Reservasi tidak valid");
        }
        this.idReservasi = idReservasi;
    }

    public Reservasi getReservasi() {
        return reservasi;
    }

    public void setReservasi(Reservasi reservasi) {
        this.reservasi = reservasi;
        if (reservasi != null) {
            this.idReservasi = reservasi.getIdReservasi();
        }
    }

    public LocalDateTime getTanggalPembayaran() {
        return tanggalPembayaran;
    }

    public void setTanggalPembayaran(LocalDateTime tanggalPembayaran) {
        if (tanggalPembayaran == null) {
            this.tanggalPembayaran = LocalDateTime.now();
        } else {
            this.tanggalPembayaran = tanggalPembayaran;
        }
    }

    public double getJumlahBayar() {
        return jumlahBayar;
    }

    public void setJumlahBayar(double jumlahBayar) {
        if (jumlahBayar < 0) {
            throw new IllegalArgumentException("Jumlah bayar tidak boleh negatif");
        }
        this.jumlahBayar = jumlahBayar;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        if (metodePembayaran == null || metodePembayaran.trim().isEmpty()) {
            throw new IllegalArgumentException("Metode pembayaran tidak boleh kosong");
        }
        this.metodePembayaran = metodePembayaran.trim();
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        if (statusPembayaran == null || statusPembayaran.trim().isEmpty()) {
            throw new IllegalArgumentException("Status pembayaran tidak boleh kosong");
        }
        this.statusPembayaran = statusPembayaran.trim();
    }

    @Override
    public String toString() {
        return "Bayar #" + idPembayaran + " - Rp " + String.format("%,.0f", jumlahBayar) + " (" + statusPembayaran + ")";
    }
}
