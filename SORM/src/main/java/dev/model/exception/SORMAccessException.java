package dev.model.exception;

/**
 * Exception for when a field marked with @SORMField could not be accessed
 * Basic wrapper for an IllegalAccessException
 */
public class SORMAccessException extends IllegalAccessException{
    public SORMAccessException(String message){
        super(message);
    }
}
