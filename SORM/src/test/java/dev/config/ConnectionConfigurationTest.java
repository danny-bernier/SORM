package dev.config;

import dev.model.exception.XMLConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.util.Map;

/**
 * Testing ConnectionConfiguration {@link ConnectionConfiguration}
 */
public class ConnectionConfigurationTest {

    @Test
    public void ConnectionConfiguration_ReadXML_FullyFilledCorrectXML(){
        Map<String, String> connectionInformation = ConnectionConfiguration.getDBConnectionInformation("./src/test/java/dev/config/DB_ConnectionTest1.xml");
        Assert.assertEquals("testingurl", connectionInformation.get("url"));
        Assert.assertEquals("testingschema", connectionInformation.get("schema"));
        Assert.assertEquals("testinguser", connectionInformation.get("user"));
        Assert.assertEquals("testingpassword", connectionInformation.get("password"));
    }

    @Test
    public void ConnectionConfiguration_ReadXML_PartiallyFilledCorrectXML(){
        Map<String, String> connectionInformation = ConnectionConfiguration.getDBConnectionInformation("./src/test/java/dev/config/DB_ConnectionTest2.xml");
        Assert.assertEquals("testingurl", connectionInformation.get("url"));
        Assert.assertEquals("", connectionInformation.get("schema"));
        Assert.assertEquals("testinguser", connectionInformation.get("user"));
        Assert.assertEquals("", connectionInformation.get("password"));
    }

    @Test()
    public void ConnectionConfiguration_ReadXML_PartiallyFilledIncorrectXML(){
        Assertions.assertThrows(XMLConfigurationException.class, () -> ConnectionConfiguration.getDBConnectionInformation("./src/test/java/dev/config/DB_ConnectionTest3.xml"));
    }

    @Test()
    public void ConnectionConfiguration_ReadXML_EmptyIncorrectXML(){
        Assertions.assertThrows(XMLConfigurationException.class, () -> ConnectionConfiguration.getDBConnectionInformation("./src/test/java/dev/config/DB_ConnectionTest4.xml"));
    }

    @Test()
    public void ConnectionConfiguration_ReadXML_FileDoesNotExist(){
        Assertions.assertThrows(XMLConfigurationException.class, () -> ConnectionConfiguration.getDBConnectionInformation("this filename doesnt exist"));
    }

    @Test
    public void ConnectionConfiguration_ReadXML_NullFile(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> ConnectionConfiguration.getDBConnectionInformation(null));
    }
}
