package model;

/**
 * Model Fasilitas merepresentasikan fasilitas tambahan pada kamar hotel.
 */
public class Fasilitas {
    private int idFasilitas;
    private String namaFasilitas;
    private double biaya;
    private String tipeKamar; // Standard, Deluxe, Suite, atau All

    public Fasilitas() {}

    public Fasilitas(int idFasilitas, String namaFasilitas, double biaya, String tipeKamar) {
        setIdFasilitas(idFasilitas);
        setNamaFasilitas(namaFasilitas);
        setBiaya(biaya);
        setTipeKamar(tipeKamar);
    }

    public Fasilitas(String namaFasilitas, double biaya, String tipeKamar) {
        this(0, namaFasilitas, biaya, tipeKamar);
    }

    public int getIdFasilitas() {
        return idFasilitas;
    }

    public void setIdFasilitas(int idFasilitas) {
        if (idFasilitas < 0) {
            throw new IllegalArgumentException("ID Fasilitas tidak boleh negatif");
        }
        this.idFasilitas = idFasilitas;
    }

    public String getNamaFasilitas() {
        return namaFasilitas;
    }

    public void setNamaFasilitas(String namaFasilitas) {
        if (namaFasilitas == null || namaFasilitas.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama fasilitas tidak boleh kosong");
        }
        this.namaFasilitas = namaFasilitas.trim();
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        if (biaya < 0) {
            throw new IllegalArgumentException("Biaya fasilitas tidak boleh negatif");
        }
        this.biaya = biaya;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        if (tipeKamar == null || tipeKamar.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipe kamar fasilitas tidak boleh kosong");
        }
        this.tipeKamar = tipeKamar.trim();
    }

    @Override
    public String toString() {
        return namaFasilitas + " (+Rp " + String.format("%,.0f", biaya) + ")";
    }
}
