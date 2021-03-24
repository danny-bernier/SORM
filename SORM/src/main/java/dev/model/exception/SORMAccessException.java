package dev.model.exception;

/**
 * Exception for when a field marked with @SORMField could not be accessed
 * Basic wrapper for an IllegalAccessException
 */
public class SORMAccessException extends IllegalAccessException{

    /**
     * Creates a new SORMAccessException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public SORMAccessException(String message){
        super(message);
    }
}
