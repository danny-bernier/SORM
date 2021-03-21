package dev.model.database;

import dev.model.enumeration.SQLDataType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * testing {@link DataField}
 */
public class DataFieldTest {
    @Test
    public void dataField_ProperDataField(){
        DataField<String> x = DataField.createDataField("Stupid String!");
        Assert.assertEquals(SQLDataType.TEXT, x.getDataType());
        Assert.assertEquals("Stupid String!", x.getValue());
    }

    @Test
    public void dataField_IllegalDataField(){
        Object x = new Object();
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField(x));
    }

    @Test
    public void dataField_NullDataField(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField(null));
    }

    @Test
    public void dataField_hashCode(){
        DataField<Integer> x = DataField.createDataField(5);
        DataField<Integer> y = DataField.createDataField(5);
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    public void dataField_Equals(){
        DataField<Integer> x = DataField.createDataField(5);
        DataField<Integer> y = DataField.createDataField(5);
        DataField<Integer> z = x;
        Object o = new Object();
        Assert.assertTrue(x.equals(y));
        Assert.assertTrue(y.equals(x));
        Assert.assertTrue(x.equals(z));
        Assert.assertTrue(z.equals(x));
        Assert.assertFalse(x.equals(null));
        Assert.assertFalse(x.equals(o));
    }

    @Test
    public void dataField_toString(){
        DataField<Integer> x = DataField.createDataField(5);
        Assert.assertEquals("DataField{value=5, dataType=INTEGER}", x.toString());
    }
}
