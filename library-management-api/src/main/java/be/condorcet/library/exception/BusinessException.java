package be.condorcet.library.exception;

/**
 * Exception métier levée quand une règle de gestion est violée.
 * Par exemple : limite d'emprunts dépassée, livre indisponible, etc.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
