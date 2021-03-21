package dev.model.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link XMLConfigurationException}
 */
public class XMLConfigurationExceptionTest {

    @Test
    public void XMLConfigurationException_validException(){
        XMLConfigurationException x = new XMLConfigurationException("potato");
        Assert.assertEquals("potato", x.getMessage());
    }
}
