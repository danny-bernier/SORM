package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link SORMAccessException}
 */
public class SORMAccessExceptionTest {

    @Test
    public void SORMAccessException_validException(){
        SORMAccessException x = new SORMAccessException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
