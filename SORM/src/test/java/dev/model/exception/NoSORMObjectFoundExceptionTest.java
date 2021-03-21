package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link NoSORMObjectFoundException}
 */
public class NoSORMObjectFoundExceptionTest {

    @Test
    public void NoSORMObjectFoundException_validException(){
        NoSORMObjectFoundException x = new NoSORMObjectFoundException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
