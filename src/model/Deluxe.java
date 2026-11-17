package model;

/**
 * Class Deluxe merepresentasikan kamar tipe deluxe dengan fasilitas tambahan.
 * Menerapkan pilar Inheritance (extends Kamar) dan Polymorphism.
 */
public class Deluxe extends Kamar {
    private double biayaFasilitas = 50000.0; // Biaya sarapan buffet & fasilitas deluxe per malam

    public Deluxe() {
        super();
        setTipeKamar("Deluxe");
    }

    public Deluxe(int idKamar, String nomorKamar, double harga, String status, int kapasitas) {
        super(idKamar, nomorKamar, "Deluxe", harga, status, kapasitas);
    }

    public Deluxe(String nomorKamar, double harga, String status, int kapasitas) {
        super(nomorKamar, "Deluxe", harga, status, kapasitas);
    }

    public Deluxe(int idKamar, String nomorKamar, double harga, String status, int kapasitas, double biayaFasilitas) {
        super(idKamar, nomorKamar, "Deluxe", harga, status, kapasitas);
        setBiayaFasilitas(biayaFasilitas);
    }

    public double getBiayaFasilitas() {
        return biayaFasilitas;
    }

    public void setBiayaFasilitas(double biayaFasilitas) {
        if (biayaFasilitas < 0) {
            throw new IllegalArgumentException("Biaya fasilitas tidak boleh negatif");
        }
        this.biayaFasilitas = biayaFasilitas;
    }

    /**
     * Implementasi perhitungan tarif untuk tipe Deluxe:
     * Tarif = (harga dasar x jumlah malam) + (biaya fasilitas x jumlah malam).
     */
    @Override
    public double hitungTarif(int jumlahMalam) {
        if (jumlahMalam < 1) {
            throw new IllegalArgumentException("Jumlah malam minimal 1");
        }
        return (getHarga() * jumlahMalam) + (biayaFasilitas * jumlahMalam);
    }
}
