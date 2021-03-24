package dev.model.exception;

/**
 * Exception for when class was not marked with @SORMObject
 * Basic wrapper for a RuntimeException
 */
public class NoSORMObjectFoundException extends RuntimeException{

    /**
     * Creates a new NoSORMObjectFoundException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public NoSORMObjectFoundException(String message){
        super(message);
    }
}
