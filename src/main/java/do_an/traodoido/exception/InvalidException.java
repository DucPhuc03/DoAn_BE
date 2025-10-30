package do_an.traodoido.exception;

public class InvalidException extends RuntimeException {
    public InvalidException(String message) {
        super(message);
    }
    
    public InvalidException(String resourceType, Long id) {
        super(String.format("%s not found with id: %d", resourceType, id));
    }
    
    public InvalidException(String resourceType, String field, String value) {
        super(String.format("%s not found with %s: %s", resourceType, field, value));
    }
}
