package dev.model.exception;

/**
 * This exception is thrown when failing to retrieve and reconstruct an object from a database
 * Simple wrapper for a {@link RuntimeException}
 */
public class SORMObjectRetrievalException extends RuntimeException {

    /**
     * Creates a new SORMObjectRetrievalException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public SORMObjectRetrievalException(String message){
        super(message);
    }
}
