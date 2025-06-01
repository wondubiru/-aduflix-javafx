package user;

public class InvalidMediaException extends Exception {

    /**
     * Constructs a new InvalidMediaException with the specified detail message.
     * @param message the detail message.
     */
    public InvalidMediaException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidMediaException with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public InvalidMediaException(String message, Throwable cause) {
        super(message, cause);
    }
}