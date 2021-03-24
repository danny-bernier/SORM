package dev.model.exception;

/**
 * Exception for when no property was marked as ID
 * Basic wrapper for a RuntimeException
 */
public class NoSORMIDFoundException extends RuntimeException{

    /**
     * Creates a new NoSORMIDFoundException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public NoSORMIDFoundException(String message){
        super(message);
    }
}
