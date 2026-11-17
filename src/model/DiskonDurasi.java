package model;

/**
 * Implementasi strategi diskon berdasarkan durasi malam menginap:
 * - 1 sampai 2 malam: 0%
 * - 3 sampai 5 malam: 5%
 * - Lebih dari 5 malam: 10%
 */
public class DiskonDurasi implements Diskon {
    private int jumlahMalam;

    public DiskonDurasi(int jumlahMalam) {
        if (jumlahMalam < 1) {
            throw new IllegalArgumentException("Jumlah malam minimal 1");
        }
        this.jumlahMalam = jumlahMalam;
    }

    public int getJumlahMalam() {
        return jumlahMalam;
    }

    public void setJumlahMalam(int jumlahMalam) {
        if (jumlahMalam < 1) {
            throw new IllegalArgumentException("Jumlah malam minimal 1");
        }
        this.jumlahMalam = jumlahMalam;
    }

    @Override
    public double getPersentase() {
        if (jumlahMalam >= 6) {
            return 0.10; // 10%
        } else if (jumlahMalam >= 3) {
            return 0.05; // 5%
        } else {
            return 0.0;  // 0%
        }
    }

    @Override
    public double hitungDiskon(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("Total biaya tidak boleh negatif");
        }
        return total * getPersentase();
    }

    @Override
    public String getNamaDiskon() {
        if (jumlahMalam >= 6) {
            return "Diskon Long-Stay (>5 Malam - 10%)";
        } else if (jumlahMalam >= 3) {
            return "Diskon Menginap Hemat (3-5 Malam - 5%)";
        } else {
            return "Tanpa Diskon Durasi";
        }
    }
}
