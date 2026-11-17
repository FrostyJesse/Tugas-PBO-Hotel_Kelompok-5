package model;

/**
 * Model Tamu merepresentasikan tamu/pelanggan hotel.
 * Menerapkan pilar Encapsulation dengan validasi ketat pada setter.
 */
public class Tamu {
    private int idTamu;
    private String nama;
    private String nomorIdentitas; // NIK / No Paspor
    private String noTelepon;
    private String email;
    private String alamat;

    public Tamu() {}

    public Tamu(int idTamu, String nama, String nomorIdentitas, String noTelepon, String email, String alamat) {
        setIdTamu(idTamu);
        setNama(nama);
        setNomorIdentitas(nomorIdentitas);
        setNoTelepon(noTelepon);
        setEmail(email);
        setAlamat(alamat);
    }

    public Tamu(String nama, String nomorIdentitas, String noTelepon, String email, String alamat) {
        this(0, nama, nomorIdentitas, noTelepon, email, alamat);
    }

    public int getIdTamu() {
        return idTamu;
    }

    public void setIdTamu(int idTamu) {
        if (idTamu < 0) {
            throw new IllegalArgumentException("ID Tamu tidak boleh negatif");
        }
        this.idTamu = idTamu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tamu tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public String getNomorIdentitas() {
        return nomorIdentitas;
    }

    public void setNomorIdentitas(String nomorIdentitas) {
        if (nomorIdentitas == null || nomorIdentitas.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor identitas (KTP/Paspor) tidak boleh kosong");
        }
        this.nomorIdentitas = nomorIdentitas.trim();
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        if (noTelepon == null || noTelepon.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor telepon tidak boleh kosong");
        }
        this.noTelepon = noTelepon.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email tidak boleh kosong");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Format email tidak valid (harus mengandung @)");
        }
        this.email = email.trim();
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = (alamat != null) ? alamat.trim() : "";
    }

    @Override
    public String toString() {
        return nama + " (" + nomorIdentitas + ")";
    }
}
