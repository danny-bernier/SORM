package dev.model.exception;

/**
 * Exception representing improper xml configuration
 * Basic wrapper for a RuntimeException
 */
public class XMLConfigurationException extends RuntimeException{

    /**
     * Creates a new XMLConfigurationException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public XMLConfigurationException(String message){
        super(message);
    }
}
