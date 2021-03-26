package dev.database.hikaricp;

import org.junit.Assert;
import org.junit.Test;

/**
 * testing {@link DataSource}
 */
public class DataSourceTest {

    @Test
    public void DataSourceSimpleTest(){
        try{
            new DataSource("jdbc:h2:mem:tcp://localhost/~/test;MODE=PostgreSQL;","sa","").getConnection();
        } catch (Exception ignored){
            Assert.fail("Exception was thrown when trying to create connection pool");
        }
        Assert.assertTrue(true);
    }
}
