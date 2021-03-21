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
        DataField<String> x = DataField.createDataField("Stupid String!","title");
        Assert.assertEquals(SQLDataType.TEXT, x.getDataType());
        Assert.assertEquals("Stupid String!", x.getValue());
        Assert.assertEquals("title", x.getValueFieldName());
    }

    @Test
    public void dataField_IllegalDataField(){
        Object x = new Object();
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField(x, "thing"));
    }

    @Test
    public void dataField_NullDataField(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField("potato", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField(null, "potato"));
    }

    @Test
    public void dataField_EmptyFieldName(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataField.createDataField("potato", ""));
    }

    @Test
    public void dataField_hashCode(){
        DataField<Integer> x = DataField.createDataField(5, "size");
        DataField<Integer> y = DataField.createDataField(5, "size");
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    public void dataField_Equals(){
        DataField<Integer> x = DataField.createDataField(5, "size");
        DataField<Integer> y = DataField.createDataField(5, "size");
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
        DataField<Integer> x = DataField.createDataField(5, "size");
        Assert.assertEquals("DataField{value=5, dataType=INTEGER}", x.toString());
    }
}
