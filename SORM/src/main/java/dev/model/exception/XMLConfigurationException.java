package dev.model.exception;

/**
 * Exception representing improper xml configuration
 * Basic wrapper for a RuntimeException
 */
public class XMLConfigurationException extends RuntimeException{
    public XMLConfigurationException(String message){
        super(message);
    }
}
