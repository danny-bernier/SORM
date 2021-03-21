package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link NoSORMIDFoundException}
 */
public class NoSORMIDFoundExceptionTest {

    @Test
    public void NoSORMIDFoundException_validException(){
        NoSORMIDFoundException x = new NoSORMIDFoundException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
