package model;

/**
 * Abstract class Kamar merepresentasikan konsep dasar kamar hotel.
 * Menerapkan pilar Encapsulation, Abstraction, dan Polymorphism.
 */
public abstract class Kamar {
    private int idKamar;
    private String nomorKamar;
    private String tipeKamar;
    private double harga; // harga dasar per malam
    private String status; // Tersedia, Dipesan, Terisi, Maintenance
    private int kapasitas;

    // Default constructor
    public Kamar() {
        this.status = "Tersedia";
        this.kapasitas = 2;
    }

    // Parameterized constructor
    public Kamar(int idKamar, String nomorKamar, String tipeKamar, double harga, String status, int kapasitas) {
        setIdKamar(idKamar);
        setNomorKamar(nomorKamar);
        setTipeKamar(tipeKamar);
        setHarga(harga);
        setStatus(status);
        setKapasitas(kapasitas);
    }

    // Constructor tanpa ID (untuk data baru sebelum auto-increment MySQL)
    public Kamar(String nomorKamar, String tipeKamar, double harga, String status, int kapasitas) {
        this(0, nomorKamar, tipeKamar, harga, status, kapasitas);
    }

    // ==========================================
    // GETTER & SETTER DENGAN VALIDASI ENKAPSULASI
    // ==========================================

    public int getIdKamar() {
        return idKamar;
    }

    public void setIdKamar(int idKamar) {
        if (idKamar < 0) {
            throw new IllegalArgumentException("ID Kamar tidak boleh negatif");
        }
        this.idKamar = idKamar;
    }

    public String getNomorKamar() {
        return nomorKamar;
    }

    public void setNomorKamar(String nomorKamar) {
        if (nomorKamar == null || nomorKamar.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong");
        }
        this.nomorKamar = nomorKamar.trim();
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        if (tipeKamar == null || tipeKamar.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipe kamar tidak boleh kosong");
        }
        this.tipeKamar = tipeKamar.trim();
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status kamar tidak boleh kosong");
        }
        this.status = status.trim();
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        if (kapasitas < 1) {
            throw new IllegalArgumentException("Kapasitas minimal 1 orang");
        }
        this.kapasitas = kapasitas;
    }

    // ==========================================
    // ABSTRACT METHOD & METHOD OVERLOADING
    // ==========================================

    /**
     * Abstract method untuk menghitung tarif sewa kamar berdasarkan durasi malam.
     * Di-override oleh subclass: Standard, Deluxe, Suite (Polymorphism - Overriding).
     * @param jumlahMalam durasi sewa
     * @return total tarif
     */
    public abstract double hitungTarif(int jumlahMalam);

    /**
     * Overloaded method untuk menghitung tarif sewa kamar dengan diskon khusus.
     * Menerapkan konsep Method Overloading.
     * @param jumlahMalam durasi sewa
     * @param diskonPersen persentase diskon (contoh 0.10 untuk 10%)
     * @return total tarif setelah diskon
     */
    public double hitungTarif(int jumlahMalam, double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 1) {
            throw new IllegalArgumentException("Persentase diskon harus di antara 0.0 dan 1.0");
        }
        double totalSebelumDiskon = hitungTarif(jumlahMalam);
        return totalSebelumDiskon - (totalSebelumDiskon * diskonPersen);
    }

    @Override
    public String toString() {
        return nomorKamar + " - " + tipeKamar + " (Rp " + String.format("%,.0f", harga) + "/malam)";
    }
}
