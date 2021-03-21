package dev.model.exception;

/**
 * Exception for when class was not marked with @SORMObject
 * Basic wrapper for a RuntimeException
 */
public class NoSORMObjectFoundException extends RuntimeException{
    public NoSORMObjectFoundException(String message){
        super(message);
    }
}
