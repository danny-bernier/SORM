package dev.model.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Testing ConnectionConfiguration {@link SQLDataType}
 */
public class SQLDataTypeTest {

    @Test
    public void SQLDataType_CHAR(){
        char x = 'A';
        Assert.assertEquals(SQLDataType.CHAR, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_TEXT(){
        String x = "This is a String!";
        Assert.assertEquals(SQLDataType.TEXT, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_BIT(){
        boolean x = true;
        Assert.assertEquals(SQLDataType.BIT, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_NUMERIC(){
        BigDecimal x = new BigDecimal("13.99992");
        Assert.assertEquals(SQLDataType.NUMERIC, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_TINYINT(){
        byte x = 5;
        Assert.assertEquals(SQLDataType.TINYINT, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_SMALLINT(){
        short x = 133;
        Assert.assertEquals(SQLDataType.SMALLINT, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_INTEGER(){
        int x = 13;
        Assert.assertEquals(SQLDataType.INTEGER, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_BIGINT(){
        long x = 1254598123;
        Assert.assertEquals(SQLDataType.BIGINT, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_REAL(){
        float x = 133.335f;
        Assert.assertEquals(SQLDataType.REAL, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_DOUBLE(){
        double x = 222.5661;
        Assert.assertEquals(SQLDataType.DOUBLE, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_DATE(){
        Date x = Date.valueOf(LocalDate.now());
        Assert.assertEquals(SQLDataType.DATE, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_TIME(){
        Time x = Time.valueOf(LocalTime.now());
        Assert.assertEquals(SQLDataType.TIME, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_TIMESTAMP(){
        Timestamp x = Timestamp.from(Instant.now());
        Assert.assertEquals(SQLDataType.TIMESTAMP, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_INVALID(){
        Object x = new Object();
        Assert.assertEquals(SQLDataType.INVALID, SQLDataType.INVALID.convertToSQLDataType(x));
    }

    @Test
    public void SQLDataType_Null(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> SQLDataType.INVALID.convertToSQLDataType(null));
    }
}
