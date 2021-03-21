package dev.utility.reflection;

import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.database.DataField;
import dev.model.enumeration.SQLDataType;
import dev.model.exception.NoSORMIDFoundException;
import dev.model.exception.NoSORMObjectFoundException;
import dev.model.exception.SORMAccessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;

/**
 * testing {@link PropertyGatherer}
 */
public class PropertyGathererTest {

    @Test
    public void PropertyGathererValidObject() throws SORMAccessException {
        @SORMObject
        class TestEmployee{
            @SORMID
            String emplID = "872DHAYU2138DJ";
            @SORMField
            String name = "Billy Bob";
            @SORMField
            int age = 45;
        }
        TestEmployee e = new TestEmployee();
        DataField<?> d = PropertyGatherer.getID(e);
        List<DataField<?>> l = PropertyGatherer.getFields(e);
        Assert.assertEquals("872DHAYU2138DJ", d.getValue());
        Assert.assertEquals(SQLDataType.TEXT, d.getDataType());
        Assert.assertEquals("Billy Bob", l.get(0).getValue());
        Assert.assertEquals(SQLDataType.TEXT, l.get(0).getDataType());
        Assert.assertEquals(45, l.get(1).getValue());
        Assert.assertEquals(SQLDataType.INTEGER, l.get(1).getDataType());
    }

    @Test
    public void PropertyGathererNoSORMObjectAnnotation() throws SORMAccessException {

        class TestEmployee2{
            @SORMID
            String emplID = "872DHAYU2138DJ";
            @SORMField
            String name = "Billy Bob";
            @SORMField
            int age = 45;
        }
        TestEmployee2 e = new TestEmployee2();
        Assertions.assertThrows(NoSORMObjectFoundException.class, () -> PropertyGatherer.getID(e));
        Assertions.assertThrows(NoSORMObjectFoundException.class, () -> PropertyGatherer.getFields(e));
    }

    @Test
    public void PropertyGathererNoSORMIDAnnotation() throws SORMAccessException {

        @SORMObject
        class TestEmployee3{
            String emplID = "872DHAYU2138DJ";
            @SORMField
            String name = "Billy Bob";
            @SORMField
            int age = 45;
        }
        TestEmployee3 e = new TestEmployee3();

        Assertions.assertThrows(NoSORMIDFoundException.class, () -> PropertyGatherer.getID(e));

        List<DataField<?>> l = PropertyGatherer.getFields(e);
        Assert.assertEquals("Billy Bob", l.get(0).getValue());
        Assert.assertEquals(SQLDataType.TEXT, l.get(0).getDataType());
        Assert.assertEquals(45, l.get(1).getValue());
        Assert.assertEquals(SQLDataType.INTEGER, l.get(1).getDataType());
    }

    @Test
    public void PropertyGathererNoFields() throws SORMAccessException {

        @SORMObject
        class TestEmployee4{
            @SORMID
            String emplID = "872DHAYU2138DJ";
            String name = "Billy Bob";
            int age = 45;
        }
        TestEmployee4 e = new TestEmployee4();
        List<DataField<?>> l = PropertyGatherer.getFields(e);
        Assert.assertEquals(0, l.size());
    }

    @Test
    public void PropertyGathererPrivateFields() throws SORMAccessException {

        @SORMObject
        class TestEmployee5{
            @SORMID
            private String emplID = "872DHAYU2138DJ";
            @SORMField
            private String name = "Billy Bob";
            @SORMField
            private int age = 45;
        }
        TestEmployee5 e = new TestEmployee5();
        DataField<?> d = PropertyGatherer.getID(e);
        List<DataField<?>> l = PropertyGatherer.getFields(e);
        Assert.assertEquals("872DHAYU2138DJ", d.getValue());
        Assert.assertEquals(SQLDataType.TEXT, d.getDataType());
        Assert.assertEquals("Billy Bob", l.get(0).getValue());
        Assert.assertEquals(SQLDataType.TEXT, l.get(0).getDataType());
        Assert.assertEquals(45, l.get(1).getValue());
        Assert.assertEquals(SQLDataType.INTEGER, l.get(1).getDataType());
    }
}
