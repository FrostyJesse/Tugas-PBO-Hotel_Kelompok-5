package model;

/**
 * Class Standard merepresentasikan kamar tipe standar.
 * Menerapkan pilar Inheritance (extends Kamar) dan Polymorphism.
 */
public class Standard extends Kamar {

    public Standard() {
        super();
        setTipeKamar("Standard");
    }

    public Standard(int idKamar, String nomorKamar, double harga, String status, int kapasitas) {
        super(idKamar, nomorKamar, "Standard", harga, status, kapasitas);
    }

    public Standard(String nomorKamar, double harga, String status, int kapasitas) {
        super(nomorKamar, "Standard", harga, status, kapasitas);
    }

    /**
     * Implementasi perhitungan tarif untuk tipe Standard:
     * Tarif = harga dasar x jumlah malam.
     */
    @Override
    public double hitungTarif(int jumlahMalam) {
        if (jumlahMalam < 1) {
            throw new IllegalArgumentException("Jumlah malam minimal 1");
        }
        return getHarga() * jumlahMalam;
    }
}
