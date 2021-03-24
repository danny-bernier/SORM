package dev.model.exception;

/**
 * Thrown when a reserved SORM keyword is used in improper context
 * <p>
 *     For example, if a field, ID, or Reference annotated property
 *     is named a reserved keyword.
 * </p>
 */
public class SORMReservedKeywordException extends RuntimeException{

    /**
     * Creates a new SORMReservedKeywordException
     * @param message  The message to be attached to this exception and to be displayed by .getMessage()
     */
    public SORMReservedKeywordException(String message){
        super(message);
    }
}
