package model;

import exception.ReservasiException;
import java.time.LocalDateTime;

/**
 * Model & Logika CheckIn menangani alur registrasi kedatangan tamu di hotel.
 */
public class CheckIn {
    private Reservasi reservasi;
    private LocalDateTime waktuCheckIn;
    private String catatanPetugas;

    public CheckIn(Reservasi reservasi) {
        if (reservasi == null) {
            throw new IllegalArgumentException("Data reservasi tidak boleh null");
        }
        this.reservasi = reservasi;
        this.waktuCheckIn = LocalDateTime.now();
        this.catatanPetugas = "Check-in berhasil dilakukan.";
    }

    public Reservasi getReservasi() {
        return reservasi;
    }

    public LocalDateTime getWaktuCheckIn() {
        return waktuCheckIn;
    }

    public String getCatatanPetugas() {
        return catatanPetugas;
    }

    public void setCatatanPetugas(String catatanPetugas) {
        this.catatanPetugas = catatanPetugas;
    }

    /**
     * Memvalidasi dan mengeksekusi perubahan status check-in:
     * - Status Reservasi -> CHECKED_IN
     * - Status Kamar -> Terisi
     */
    public void prosesCheckIn() throws ReservasiException {
        if ("CHECKED_IN".equalsIgnoreCase(reservasi.getStatusReservasi())) {
            throw new ReservasiException("Reservasi #" + reservasi.getIdReservasi() + " sudah dalam status CHECKED_IN!");
        }
        if ("CHECKED_OUT".equalsIgnoreCase(reservasi.getStatusReservasi())) {
            throw new ReservasiException("Reservasi sudah selesai (CHECKED_OUT) dan tidak dapat check-in kembali.");
        }
        if ("BATAL".equalsIgnoreCase(reservasi.getStatusReservasi())) {
            throw new ReservasiException("Reservasi telah dibatalkan.");
        }

        if (reservasi.getKamar() != null) {
            String statusKamar = reservasi.getKamar().getStatus();
            if ("Terisi".equalsIgnoreCase(statusKamar)) {
                throw new ReservasiException("Kamar " + reservasi.getKamar().getNomorKamar() + " sedang terisi oleh tamu lain!");
            }
            if ("Maintenance".equalsIgnoreCase(statusKamar)) {
                throw new ReservasiException("Kamar " + reservasi.getKamar().getNomorKamar() + " sedang dalam masa maintenance/perbaikan!");
            }
            reservasi.getKamar().setStatus("Terisi");
        }

        reservasi.setStatusReservasi("CHECKED_IN");
        this.waktuCheckIn = LocalDateTime.now();
    }
}
