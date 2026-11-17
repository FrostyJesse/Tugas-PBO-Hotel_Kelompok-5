package model;

/**
 * Implementasi Diskon untuk Member / Pelanggan Loyal (VIP):
 * Memberikan diskon tetap 15% dari total harga.
 */
public class DiskonMember implements Diskon {
    private String tipeMember; // e.g. "VIP", "REGULAR"

    public DiskonMember(String tipeMember) {
        this.tipeMember = (tipeMember != null && !tipeMember.trim().isEmpty()) ? tipeMember : "REGULAR";
    }

    public String getTipeMember() {
        return tipeMember;
    }

    public void setTipeMember(String tipeMember) {
        this.tipeMember = tipeMember;
    }

    @Override
    public double getPersentase() {
        if ("VIP".equalsIgnoreCase(tipeMember)) {
            return 0.15; // 15%
        } else if ("GOLD".equalsIgnoreCase(tipeMember)) {
            return 0.10; // 10%
        }
        return 0.05; // 5% regular member
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
        return "Diskon Keanggotaan Member (" + tipeMember.toUpperCase() + " - " + (int)(getPersentase() * 100) + "%)";
    }
}
