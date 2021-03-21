package dev.database;

import dev.config.ConnectionConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Testing ConnectionConfiguration {@link DBConnection}
 */
public class DBConnection1Test {

    //unfortunately when testing a singleton, only one test can be run at a time,
    // comment the other one and uncomment this one to test it
//    @Test
//    public void DBConnection_GetConnection_WithSchema() {
//        Map<String, String> testMap = new HashMap<>();
//        testMap.put("url", "jdbc:h2:mem:tcp://localhost/~/test");
//        testMap.put("schema", "public");
//        testMap.put("user", "sa");
//        testMap.put("password", "");
//
//        try(MockedStatic<ConnectionConfiguration> mockedConnectionConfiguration = Mockito.mockStatic(ConnectionConfiguration.class)) {
//            mockedConnectionConfiguration.when(() -> ConnectionConfiguration.getDBConnectionInformation(Mockito.anyString())).thenReturn(testMap);
//            DBConnection.getInstance().getConnection();
//            try{
//                mockedConnectionConfiguration.verify(() -> ConnectionConfiguration.getDBConnectionInformation(Mockito.anyString()));
//            }catch (Exception e){
//                Assert.fail("Mock failed to be called: " + e.getMessage());
//            }
//        } catch (Exception e){
//            Assert.fail("An exception was thrown when trying to create a connection: " + e.getMessage());
//        }
//        Assert.assertTrue(true);
//    }

    @Test
    public void DBConnection_GetConnection_NoSchema(){
        Map<String, String> testMap = new HashMap<>();
        testMap.put("url", "jdbc:h2:mem:tcp://localhost/~/test");
        testMap.put("user", "sa");
        testMap.put("password", "");

        try(MockedStatic<ConnectionConfiguration> mockedConnectionConfiguration = Mockito.mockStatic(ConnectionConfiguration.class)) {
            mockedConnectionConfiguration.when(() -> ConnectionConfiguration.getDBConnectionInformation(Mockito.anyString())).thenReturn(testMap);
            DBConnection.getInstance().getConnection();
            try{
                mockedConnectionConfiguration.verify(() -> ConnectionConfiguration.getDBConnectionInformation(Mockito.anyString()));
            }catch (Exception e){
                Assert.fail("Mock failed to be called: " + e.getMessage());
            }
        } catch (Exception e){
            Assert.fail("An exception was thrown when trying to create a connection: " + e.getMessage());
        }

        Assert.assertTrue(true);
    }
}
