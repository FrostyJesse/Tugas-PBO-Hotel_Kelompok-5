package exception;

/**
 * Custom Exception untuk kesalahan operasional reservasi, check-in, check-out,
 * dan ketersediaan kamar.
 */
public class ReservasiException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReservasiException(String message) {
        super(message);
    }

    public ReservasiException(String message, Throwable cause) {
        super(message, cause);
    }
}
