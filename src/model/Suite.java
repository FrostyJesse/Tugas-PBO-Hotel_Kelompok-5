package model;

/**
 * Class Suite merepresentasikan kamar tipe suite termewah dengan fasilitas premium.
 * Menerapkan pilar Inheritance (extends Kamar) dan Polymorphism.
 */
public class Suite extends Kamar {
    private double biayaFasilitasPremium = 150000.0; // Biaya jacuzzi, VIP lounge & shuttle per malam

    public Suite() {
        super();
        setTipeKamar("Suite");
    }

    public Suite(int idKamar, String nomorKamar, double harga, String status, int kapasitas) {
        super(idKamar, nomorKamar, "Suite", harga, status, kapasitas);
    }

    public Suite(String nomorKamar, double harga, String status, int kapasitas) {
        super(nomorKamar, "Suite", harga, status, kapasitas);
    }

    public Suite(int idKamar, String nomorKamar, double harga, String status, int kapasitas, double biayaFasilitasPremium) {
        super(idKamar, nomorKamar, "Suite", harga, status, kapasitas);
        setBiayaFasilitasPremium(biayaFasilitasPremium);
    }

    public double getBiayaFasilitasPremium() {
        return biayaFasilitasPremium;
    }

    public void setBiayaFasilitasPremium(double biayaFasilitasPremium) {
        if (biayaFasilitasPremium < 0) {
            throw new IllegalArgumentException("Biaya fasilitas premium tidak boleh negatif");
        }
        this.biayaFasilitasPremium = biayaFasilitasPremium;
    }

    /**
     * Implementasi perhitungan tarif untuk tipe Suite:
     * Tarif = (harga dasar x jumlah malam) + (biaya fasilitas premium x jumlah malam).
     */
    @Override
    public double hitungTarif(int jumlahMalam) {
        if (jumlahMalam < 1) {
            throw new IllegalArgumentException("Jumlah malam minimal 1");
        }
        return (getHarga() * jumlahMalam) + (biayaFasilitasPremium * jumlahMalam);
    }
}
