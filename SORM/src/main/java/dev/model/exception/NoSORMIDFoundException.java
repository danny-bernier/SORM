package dev.model.exception;

/**
 * Exception for when no property was marked as ID
 * Basic wrapper for a RuntimeException
 */
public class NoSORMIDFoundException extends RuntimeException{
    public NoSORMIDFoundException(String message){
        super(message);
    }
}
