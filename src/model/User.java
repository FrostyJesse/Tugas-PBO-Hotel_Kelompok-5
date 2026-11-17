package model;

/**
 * Model User merepresentasikan akun petugas/resepsionis sistem.
 * Menerapkan pilar Encapsulation dengan validasi atribut.
 */
public class User {
    private int idUser;
    private String username;
    private String password;
    private String role; // ADMIN, RESEPSIONIS

    public User() {}

    public User(int idUser, String username, String password, String role) {
        setIdUser(idUser);
        setUsername(username);
        setPassword(password);
        setRole(role);
    }

    public User(String username, String password, String role) {
        this(0, username, password, role);
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        if (idUser < 0) {
            throw new IllegalArgumentException("ID User tidak boleh negatif");
        }
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }
        this.password = password.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role tidak boleh kosong");
        }
        this.role = role.trim();
    }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
