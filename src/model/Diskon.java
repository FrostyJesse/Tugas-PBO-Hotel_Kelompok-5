package model;

/**
 * Interface Diskon mendefinisikan kontrak perhitungan diskon
 * untuk reservasi kamar hotel (menerapkan pilar Abstraction).
 */
public interface Diskon {
    /**
     * Menghitung nilai potongan diskon dari total biaya.
     * @param total biaya awal sebelum diskon
     * @return besar potongan nominal diskon
     */
    double hitungDiskon(double total);

    /**
     * Mendapatkan nama atau label jenis diskon.
     * @return nama diskon
     */
    String getNamaDiskon();

    /**
     * Mendapatkan persentase diskon (dalam rentang 0.0 sampai 1.0).
     * @return persentase diskon
     */
    double getPersentase();
}
