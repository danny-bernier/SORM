package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link SORMObjectRetrievalExceptionTest}
 */
public class SORMObjectRetrievalExceptionTest {

    @Test
    public void SORMObjectRetrievalException_validException(){
        SORMObjectRetrievalException x = new SORMObjectRetrievalException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
