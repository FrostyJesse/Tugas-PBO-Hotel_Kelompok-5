package model;

import exception.ReservasiException;
import java.time.LocalDateTime;

/**
 * Model & Logika CheckOut menangani alur kepulangan tamu:
 * - Mengembalikan status kamar menjadi 'Tersedia'
 * - Mengubah status reservasi menjadi 'CHECKED_OUT'
 * - Menghasilkan rincian ringkasan akhir tagihan menginap
 */
public class CheckOut {
    private Reservasi reservasi;
    private LocalDateTime waktuCheckOut;
    private double biayaTambahanAtauDenda; // denda kerusakan fasilitas / minibar tambahan
    private String ringkasanTagihan;

    public CheckOut(Reservasi reservasi) {
        if (reservasi == null) {
            throw new IllegalArgumentException("Data reservasi tidak boleh null");
        }
        this.reservasi = reservasi;
        this.waktuCheckOut = LocalDateTime.now();
        this.biayaTambahanAtauDenda = 0.0;
    }

    public Reservasi getReservasi() {
        return reservasi;
    }

    public LocalDateTime getWaktuCheckOut() {
        return waktuCheckOut;
    }

    public double getBiayaTambahanAtauDenda() {
        return biayaTambahanAtauDenda;
    }

    public void setBiayaTambahanAtauDenda(double biayaTambahanAtauDenda) {
        if (biayaTambahanAtauDenda < 0) {
            throw new IllegalArgumentException("Biaya tambahan atau denda tidak boleh negatif");
        }
        this.biayaTambahanAtauDenda = biayaTambahanAtauDenda;
    }

    /**
     * Memvalidasi dan memproses alur check-out:
     * - Status Reservasi -> CHECKED_OUT
     * - Status Kamar -> Tersedia
     */
    public void prosesCheckOut() throws ReservasiException {
        if (!"CHECKED_IN".equalsIgnoreCase(reservasi.getStatusReservasi())) {
            throw new ReservasiException("Check-out hanya dapat dilakukan untuk tamu yang berstatus CHECKED_IN!");
        }

        if (reservasi.getKamar() != null) {
            reservasi.getKamar().setStatus("Tersedia");
        }

        reservasi.setStatusReservasi("CHECKED_OUT");
        this.waktuCheckOut = LocalDateTime.now();
        generateRingkasan();
    }

    /**
     * Menghasilkan teks ringkasan checkout lengkap sesuai spesifikasi tugas.
     */
    public String generateRingkasan() {
        int malam = reservasi.hitungJumlahMalam();
        Kamar k = reservasi.getKamar();
        Tamu t = reservasi.getTamu();

        double tarifDasar = (k != null) ? k.getHarga() : 0;
        double subtotal = (k != null) ? k.hitungTarif(malam) : 0;
        Diskon diskon = new DiskonDurasi(malam);
        double potongan = diskon.hitungDiskon(subtotal);
        double totalAkhir = subtotal - potongan + biayaTambahanAtauDenda;

        StringBuilder sb = new StringBuilder();
        sb.append("================ RINGKASAN CHECK-OUT ================\n");
        sb.append("ID Reservasi       : #").append(reservasi.getIdReservasi()).append("\n");
        sb.append("Nama Tamu          : ").append(t != null ? t.getNama() : "ID: " + reservasi.getIdTamu()).append("\n");
        sb.append("No. Identitas      : ").append(t != null ? t.getNomorIdentitas() : "-").append("\n");
        sb.append("Nomor Kamar        : ").append(k != null ? k.getNomorKamar() : "ID: " + reservasi.getIdKamar()).append("\n");
        sb.append("Tipe Kamar         : ").append(k != null ? k.getTipeKamar() : "-").append("\n");
        sb.append("Tanggal Check-In   : ").append(reservasi.getTanggalCheckin()).append("\n");
        sb.append("Tanggal Check-Out  : ").append(reservasi.getTanggalCheckout()).append("\n");
        sb.append("Jumlah Malam       : ").append(malam).append(" malam\n");
        sb.append("Tarif Kamar/Malam  : Rp ").append(String.format("%,.0f", tarifDasar)).append("\n");
        sb.append("Subtotal Biaya     : Rp ").append(String.format("%,.0f", subtotal)).append("\n");
        sb.append("Potongan Diskon    : - Rp ").append(String.format("%,.0f", potongan)).append(" (").append(diskon.getNamaDiskon()).append(")\n");
        if (biayaTambahanAtauDenda > 0) {
            sb.append("Biaya Tambahan/Denda: + Rp ").append(String.format("%,.0f", biayaTambahanAtauDenda)).append("\n");
        }
        sb.append("-----------------------------------------------------\n");
        sb.append("TOTAL TAGIHAN AKHIR: Rp ").append(String.format("%,.0f", totalAkhir)).append("\n");
        sb.append("Status Reservasi   : CHECKED_OUT\n");
        sb.append("Status Kamar Baru  : Tersedia\n");
        sb.append("Waktu Transaksi    : ").append(waktuCheckOut).append("\n");
        sb.append("=====================================================");

        this.ringkasanTagihan = sb.toString();
        return this.ringkasanTagihan;
    }

    public String getRingkasanTagihan() {
        if (ringkasanTagihan == null) {
            generateRingkasan();
        }
        return ringkasanTagihan;
    }
}
