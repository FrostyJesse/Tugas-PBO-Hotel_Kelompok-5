package exception;

/**
 * Custom Exception untuk kesalahan validasi input pengguna
 * (misal data kosong, format salah, harga negatif, dll).
 */
public class DataValidationException extends Exception {
    private static final long serialVersionUID = 1L;

    public DataValidationException(String message) {
        super(message);
    }
}
