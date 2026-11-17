package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Model Reservasi merepresentasikan transaksi pemesanan kamar hotel.
 * Menerapkan relasi Asosiasi dengan Tamu dan Kamar.
 */
public class Reservasi {
    private int idReservasi;
    private int idTamu;
    private int idKamar;
    private Tamu tamu;
    private Kamar kamar;
    private LocalDate tanggalCheckin;
    private LocalDate tanggalCheckout;
    private int jumlahTamu;
    private String statusReservasi; // PENDING, CHECKED_IN, CHECKED_OUT, BATAL
    private double totalHarga;

    public Reservasi() {
        this.statusReservasi = "PENDING";
        this.jumlahTamu = 1;
    }

    public Reservasi(int idReservasi, int idTamu, int idKamar, LocalDate tanggalCheckin, 
                     LocalDate tanggalCheckout, int jumlahTamu, String statusReservasi, double totalHarga) {
        setIdReservasi(idReservasi);
        setIdTamu(idTamu);
        setIdKamar(idKamar);
        setTanggalCheckin(tanggalCheckin);
        setTanggalCheckout(tanggalCheckout);
        setJumlahTamu(jumlahTamu);
        setStatusReservasi(statusReservasi);
        setTotalHarga(totalHarga);
    }

    public Reservasi(int idTamu, int idKamar, LocalDate tanggalCheckin, 
                     LocalDate tanggalCheckout, int jumlahTamu, String statusReservasi, double totalHarga) {
        this(0, idTamu, idKamar, tanggalCheckin, tanggalCheckout, jumlahTamu, statusReservasi, totalHarga);
    }

    // ==========================================
    // GETTER & SETTER DENGAN VALIDASI ENKAPSULASI
    // ==========================================

    public int getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(int idReservasi) {
        if (idReservasi < 0) {
            throw new IllegalArgumentException("ID Reservasi tidak boleh negatif");
        }
        this.idReservasi = idReservasi;
    }

    public int getIdTamu() {
        return idTamu;
    }

    public void setIdTamu(int idTamu) {
        if (idTamu < 0) {
            throw new IllegalArgumentException("ID Tamu tidak valid");
        }
        this.idTamu = idTamu;
    }

    public int getIdKamar() {
        return idKamar;
    }

    public void setIdKamar(int idKamar) {
        if (idKamar < 0) {
            throw new IllegalArgumentException("ID Kamar tidak valid");
        }
        this.idKamar = idKamar;
    }

    public Tamu getTamu() {
        return tamu;
    }

    public void setTamu(Tamu tamu) {
        this.tamu = tamu;
        if (tamu != null) {
            this.idTamu = tamu.getIdTamu();
        }
    }

    public Kamar getKamar() {
        return kamar;
    }

    public void setKamar(Kamar kamar) {
        this.kamar = kamar;
        if (kamar != null) {
            this.idKamar = kamar.getIdKamar();
        }
    }

    public LocalDate getTanggalCheckin() {
        return tanggalCheckin;
    }

    public void setTanggalCheckin(LocalDate tanggalCheckin) {
        if (tanggalCheckin == null) {
            throw new IllegalArgumentException("Tanggal check-in tidak boleh kosong");
        }
        this.tanggalCheckin = tanggalCheckin;
    }

    public LocalDate getTanggalCheckout() {
        return tanggalCheckout;
    }

    public void setTanggalCheckout(LocalDate tanggalCheckout) {
        if (tanggalCheckout == null) {
            throw new IllegalArgumentException("Tanggal check-out tidak boleh kosong");
        }
        if (this.tanggalCheckin != null && !tanggalCheckout.isAfter(this.tanggalCheckin)) {
            throw new IllegalArgumentException("Tanggal check-out harus lebih besar dari tanggal check-in");
        }
        this.tanggalCheckout = tanggalCheckout;
    }

    public int getJumlahTamu() {
        return jumlahTamu;
    }

    public void setJumlahTamu(int jumlahTamu) {
        if (jumlahTamu < 1) {
            throw new IllegalArgumentException("Jumlah tamu minimal 1 orang");
        }
        this.jumlahTamu = jumlahTamu;
    }

    public String getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(String statusReservasi) {
        if (statusReservasi == null || statusReservasi.trim().isEmpty()) {
            throw new IllegalArgumentException("Status reservasi tidak boleh kosong");
        }
        this.statusReservasi = statusReservasi.trim();
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        if (totalHarga < 0) {
            throw new IllegalArgumentException("Total harga tidak boleh negatif");
        }
        this.totalHarga = totalHarga;
    }

    // ==========================================
    // LOGIKA PERHITUNGAN RESERVASI
    // ==========================================

    /**
     * Menghitung total durasi malam menginap.
     * @return jumlah malam (minimal 1)
     */
    public int hitungJumlahMalam() {
        if (tanggalCheckin == null || tanggalCheckout == null) {
            return 1;
        }
        long days = ChronoUnit.DAYS.between(tanggalCheckin, tanggalCheckout);
        return days <= 0 ? 1 : (int) days;
    }

    /**
     * Menghitung tarif otomatis menggunakan Polimorfisme Kamar dan Diskon.
     */
    public void kalkulasiTotal() {
        if (kamar != null) {
            int malam = hitungJumlahMalam();
            // 1. Hitung tarif dasar polymorphic (Standard/Deluxe/Suite)
            double subtotal = kamar.hitungTarif(malam);

            // 2. Terapkan strategi diskon durasi
            Diskon diskonDurasi = new DiskonDurasi(malam);
            double potongan = diskonDurasi.hitungDiskon(subtotal);

            this.totalHarga = subtotal - potongan;
        }
    }

    @Override
    public String toString() {
        return "Reservasi #" + idReservasi + " - Tamu: " + (tamu != null ? tamu.getNama() : idTamu) + " (" + statusReservasi + ")";
    }
}
