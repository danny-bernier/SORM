package dev.utility.reflection;

import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.annotation.SORMReference;
import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.enumeration.SQLDataType;
import dev.model.exception.NoSORMIDFoundException;
import dev.model.exception.NoSORMObjectFoundException;
import dev.model.exception.SORMAccessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;

/**
 * testing {@link POJOPropertyGetSet}
 */
public class POJOPropertyGetSetTest {

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
        DataField<?> d = POJOPropertyGetSet.getID(e);
        List<DataField<Object>> l = POJOPropertyGetSet.getFields(e);
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
        Assertions.assertThrows(NoSORMObjectFoundException.class, () -> POJOPropertyGetSet.getID(e));
        Assertions.assertThrows(NoSORMObjectFoundException.class, () -> POJOPropertyGetSet.getFields(e));
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

        Assertions.assertThrows(NoSORMIDFoundException.class, () -> POJOPropertyGetSet.getID(e));

        List<DataField<Object>> l = POJOPropertyGetSet.getFields(e);
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
        List<DataField<Object>> l = POJOPropertyGetSet.getFields(e);
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
        DataField<?> d = POJOPropertyGetSet.getID(e);
        List<DataField<Object>> l = POJOPropertyGetSet.getFields(e);
        Assert.assertEquals("872DHAYU2138DJ", d.getValue());
        Assert.assertEquals(SQLDataType.TEXT, d.getDataType());
        Assert.assertEquals("Billy Bob", l.get(0).getValue());
        Assert.assertEquals(SQLDataType.TEXT, l.get(0).getDataType());
        Assert.assertEquals(45, l.get(1).getValue());
        Assert.assertEquals(SQLDataType.INTEGER, l.get(1).getDataType());
    }

    @Test
    public void PropertyGathererReferenceFields() throws SORMAccessException {

        @SORMObject
        class TestReference{
            @SORMID
            private int key;
            public TestReference(int i){
                this.key = i;
            }
        }
        @SORMObject
        class TestEmployee6{
            @SORMID
            private String emplID = "872DHAYU2138DJ";
            @SORMField
            private String name = "Billy Bob";
            @SORMField
            private int age = 45;
            @SORMReference
            TestReference o = new TestReference(3);
            @SORMReference
            TestReference o2 = new TestReference(123);
        }
        TestEmployee6 e = new TestEmployee6();
        List<DataReference<Object>> l = POJOPropertyGetSet.getReference(e);
        Assert.assertEquals(2, l.size());
        Assert.assertEquals(e.o, l.get(0).getREFERENCE());
        Assert.assertEquals(e.o2, l.get(1).getREFERENCE());
    }

    @Test
    public void PropertyGathererNoReferenceFields() throws SORMAccessException {

        @SORMObject
        class TestEmployee6{
            @SORMID
            private String emplID = "872DHAYU2138DJ";
            @SORMField
            private String name = "Billy Bob";
            @SORMField
            private int age = 45;
        }
        TestEmployee6 e = new TestEmployee6();
        List<DataReference<Object>> l = POJOPropertyGetSet.getReference(e);
        Assert.assertEquals(0, l.size());
    }

    @Test
    public void PropertyGathererReferenceFieldsButNoSORMObject() {

        class TestEmployee7{
            @SORMID
            private String emplID = "872DHAYU2138DJ";
            @SORMField
            private String name = "Billy Bob";
            @SORMField
            private int age = 45;
            @SORMReference
            Object o = new Object();
            @SORMReference
            Object o2 = new Object();
        }
        TestEmployee7 e = new TestEmployee7();
        Assertions.assertThrows(NoSORMObjectFoundException.class, () -> POJOPropertyGetSet.getReference(e));
    }
}
