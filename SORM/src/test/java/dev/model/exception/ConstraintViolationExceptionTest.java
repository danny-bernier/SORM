package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link ConstraintViolationException}
 */
public class ConstraintViolationExceptionTest {

    @Test
    public void ConstraintViolationException_validException(){
        ConstraintViolationException x = new ConstraintViolationException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
