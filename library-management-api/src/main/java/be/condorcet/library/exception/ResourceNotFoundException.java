package be.condorcet.library.exception;

/**
 * Exception levée quand une ressource (Author, Book, Member, etc.) n'est pas trouvée.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
