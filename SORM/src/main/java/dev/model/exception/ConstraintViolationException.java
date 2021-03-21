package dev.model.exception;

/**
 * Exception representing a violation of some field's constraints in a database
 * Basic wrapper for a RuntimeException
 */
public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message){
        super(message);
    }
}
