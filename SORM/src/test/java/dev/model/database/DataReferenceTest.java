package dev.model.database;

import dev.model.enumeration.SQLDataType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * testing {@link DataReference}
 */
public class DataReferenceTest {

    @Test
    public void dataReference_ProperDataReference(){
        int i = 13;
        DataField<Object> df = DataField.createDataField(i,"object");
        DataReference<String> x = DataReference.createDataReference("Stupid String!","title", df);
        Assert.assertEquals(SQLDataType.INTEGER, x.getREFERENCES_ID().getDataType());
        Assert.assertEquals("Stupid String!", x.getREFERENCE());
        Assert.assertEquals("title", x.getREFERENCE_NAME());
    }

    @Test
    public void dataReference_IllegalDataReference(){
        int i = 13;
        DataField<Object> df = DataField.createDataField(i,"object");
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataReference.createDataReference(null,"title", df));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataReference.createDataReference("Potato",null, df));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataReference.createDataReference("Potato","", df));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataReference.createDataReference("Potato","title", null));
    }

    @Test
    public void dataReference_hashCode(){
        DataField<Object> df = DataField.createDataField(13,"object");
        DataReference<Integer> x = DataReference.createDataReference(5, "size", df);
        DataReference<Integer> y = DataReference.createDataReference(5, "size", df);
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    public void dataReference_Equals(){
        DataField<Object> df = DataField.createDataField(13,"object");
        DataReference<Integer> x = DataReference.createDataReference(5, "size", df);
        DataReference<Integer> y = DataReference.createDataReference(5, "size", df);
        DataReference<Integer> z = x;
        Object o = new Object();
        Assert.assertTrue(x.equals(y));
        Assert.assertTrue(y.equals(x));
        Assert.assertTrue(x.equals(z));
        Assert.assertTrue(z.equals(x));
        Assert.assertFalse(x.equals(null));
        Assert.assertFalse(x.equals(o));
    }

    @Test
    public void dataReference_toString(){
        DataField<Object> df = DataField.createDataField(13,"object");
        DataReference<Integer> x = DataReference.createDataReference(5, "size", df);
        Assert.assertEquals("DataReference{REFERENCE=5, REFERENCE_NAME='size', REFERENCES_ID=DataField{value=13, dataType=INTEGER}}", x.toString());
    }
}
