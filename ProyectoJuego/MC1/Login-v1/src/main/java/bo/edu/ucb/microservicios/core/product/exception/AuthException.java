package bo.edu.ucb.microservicios.core.product.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
