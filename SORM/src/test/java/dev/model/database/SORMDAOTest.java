package dev.model.database;

import dev.database.SORMDAO;
import dev.model.annotation.*;
import dev.model.exception.SORMAccessException;
import org.junit.Assert;
import org.junit.Test;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

public class SORMDAOTest {

    @Test
    public void SORMDAOCreatingCascadingTablesWithReferences() throws SQLException, SORMAccessException {
        SORMDAO<Potato, Integer> dao = new SORMDAO<>(Potato.class, Integer.class);
        Potato p = new Potato("Bobby Fisher", 31, 77);
        Assert.assertTrue(dao.create(p));
        Optional<Potato> p2 = dao.getById(p.spudID);
        Assert.assertTrue(p2.isPresent());
        Assert.assertEquals("Bobby Fisher", p2.get().name);
        Assert.assertEquals(31, p2.get().spudID);
        Assert.assertEquals(p.potatoReference.babyCode, p2.get().potatoReference.babyCode, 0.0);
    }

    @Test
    public void SORMDAOUpdatingObjectAfterCreating() throws SQLException, SORMAccessException {
        SORMDAO<Potato, Integer> dao = new SORMDAO<>(Potato.class, Integer.class);
        Potato p = new Potato("Jenkins", 16, 24);
        dao.create(p);
        Optional<Potato> po = dao.getById(p.spudID);
        Assert.assertTrue(po.isPresent());
        Assert.assertEquals("Jenkins", po.get().name);
        Potato p2 = new Potato("Billy Bob", 16, 24);
        dao.update(p2);
        po = dao.getById(p.spudID);
        Assert.assertTrue(po.isPresent());
        Assert.assertEquals("Billy Bob", po.get().name);
    }

    @Test
    public void SORMDAOUpdatingObjectReferenceAfterCreating() throws SQLException, SORMAccessException {
        SORMDAO<Potato, Integer> dao = new SORMDAO<>(Potato.class, Integer.class);
        Potato p = new Potato("Jane", 13,99);
        dao.create(p);
        Optional<Potato> po = dao.getById(p.spudID);
        Assert.assertTrue(po.isPresent());
        Assert.assertEquals("Jane", p.name);
        Assert.assertEquals(99, p.potatoReference.babyCode);
        p.potatoReference = new PotatoReference(75);
        dao.update(p);
        po = dao.getById(p.spudID);
        Assert.assertTrue(po.isPresent());
        Assert.assertEquals("Jane", p.name);
        Assert.assertEquals(75, p.potatoReference.babyCode);
    }

    @Test
    public void SORMDeleteObjectAfterCreating() throws SQLException, SORMAccessException {
        SORMDAO<Potato, Integer> dao = new SORMDAO<>(Potato.class, Integer.class);
        Potato p = new Potato("Tobby",255,13);
        dao.create(p);
        Optional<Potato> po = dao.getById(255);
        Assert.assertTrue(po.isPresent());
        Assert.assertEquals("Tobby", po.get().name);
        dao.delete(p);
        po = dao.getById(p.spudID);
        Assert.assertFalse(po.isPresent());
    }

    @Test
    public void SORMCreateChar() throws SQLException, SORMAccessException {
        SORMDAO<CharObject, Character> dao = new SORMDAO<>(CharObject.class, Character.class);
        CharObject x = new CharObject();
        dao.create(x);
        Optional<CharObject> xo = dao.getById('a');
        Assert.assertTrue(xo.isPresent());
        Assert.assertEquals('a', xo.get().id);
    }

    @Test
    public void SORMCreateLong() throws SQLException, SORMAccessException {
        SORMDAO<LongObject, Long> dao = new SORMDAO<>(LongObject.class, Long.class);
        LongObject x = new LongObject();
        dao.create(x);
        Optional<LongObject> xo = dao.getById(2333415L);
        Assert.assertTrue(xo.isPresent());
        Assert.assertEquals(2333415L, xo.get().id);
    }

//    @Test
//    public void SORMCreateFloat() throws SQLException, SORMAccessException {
//        SORMDAO<FloatObject, Float> dao = new SORMDAO<>(FloatObject.class, Float.class);
//        FloatObject x = new FloatObject();
//        dao.create(x);
//        Optional<FloatObject> xo = dao.getById(23334.15F);
//        Assert.assertTrue(xo.isPresent());
//        Assert.assertEquals(23334.15F, xo.get().id, 0.0);
//    }
//
//    @Test
//    public void SORMCreateShort() throws SQLException, SORMAccessException {
//        SORMDAO<ShortObject, Short> dao = new SORMDAO<>(ShortObject.class, Short.class);
//        ShortObject x = new ShortObject();
//        dao.create(x);
//        short y = 123;
//        Optional<ShortObject> xo = dao.getById(y);
//        Assert.assertTrue(xo.isPresent());
//        Assert.assertEquals(y, (short) xo.get().id);
//    }
//
//    @Test
//    public void SORMCreateByte() throws SQLException, SORMAccessException {
//        SORMDAO<ByteObject, Byte> dao = new SORMDAO<>(ByteObject.class, Byte.class);
//        ByteObject x = new ByteObject();
//        dao.create(x);
//        byte y = 4;
//        Optional<ByteObject> xo = dao.getById(y);
//        Assert.assertTrue(xo.isPresent());
//        Assert.assertEquals(y, (byte)xo.get().id);
//    }

    @Test
    public void SORMCreateBoolean() throws SQLException, SORMAccessException {
        SORMDAO<BooleanObject, Integer> dao = new SORMDAO<>(BooleanObject.class, Integer.class);
        BooleanObject x = new BooleanObject();
        dao.create(x);
        Optional<BooleanObject> xo = dao.getById(2);
        Assert.assertTrue(xo.isPresent());
        Assert.assertTrue(xo.get().it);
    }

    @Test
    public void SORMCreateBigDecimal() throws SQLException, SORMAccessException {
        SORMDAO<BigDecimalObject, BigDecimal> dao = new SORMDAO<>(BigDecimalObject.class, BigDecimal.class);
        BigDecimalObject x = new BigDecimalObject();
        dao.create(x);
        Optional<BigDecimalObject> xo = dao.getById(new BigDecimal("125.355"));
        Assert.assertTrue(xo.isPresent());
        Assert.assertEquals(new BigDecimal("125.355"), xo.get().id);
    }

    @Test
    public void SORMCreateDouble() throws SQLException, SORMAccessException {
        SORMDAO<DoubleObject, Double> dao = new SORMDAO<>(DoubleObject.class, Double.class);
        DoubleObject x = new DoubleObject();
        dao.create(x);
        Optional<DoubleObject> xo = dao.getById(13.13);
        Assert.assertTrue(xo.isPresent());
        Assert.assertEquals(13.13, xo.get().id, 0.0);
    }
}

@SORMObject
class PotatoReference {
    @SORMID
    int babyCode = 31;
    @SORMNoArgConstructor
    private PotatoReference(){}
    public PotatoReference(int code){
        this.babyCode = code;
    }
}

@SORMObject
class Potato {
    @SORMID
    int spudID = 0;
    @SORMField
    String name = "";
    @SORMReference
    PotatoReference potatoReference = new PotatoReference(0);

    @SORMNoArgConstructor
    private Potato(){}
    public Potato(String name,int id, int babyCode){
        this.name = name;
        this.spudID = id;
        this.potatoReference = new PotatoReference(babyCode);
    }
}

@SORMObject
class CharObject{
    @SORMID
    char id = 'a';
    @SORMNoArgConstructor
    public CharObject(){}
}

@SORMObject
class LongObject{
    @SORMID
    long id = 2333415;
    @SORMNoArgConstructor
    public LongObject(){}
}

@SORMObject
class FloatObject{
    @SORMID
    float id = 23334.15F;
    @SORMNoArgConstructor
    public FloatObject(){}
}

@SORMObject
class ShortObject{
    @SORMID
    Short id = 123;
    @SORMNoArgConstructor
    public ShortObject(){}
}

@SORMObject
class ByteObject{
    @SORMID
    Byte id = 4;
    @SORMNoArgConstructor
    public ByteObject(){}
}

@SORMObject
class BooleanObject{
    @SORMID
    int id = 2;
    @SORMField
    boolean it = true;
    @SORMNoArgConstructor
    public BooleanObject(){}
}

@SORMObject
class BigDecimalObject{
    @SORMID
    BigDecimal id = new BigDecimal("125.355");
    @SORMNoArgConstructor
    public BigDecimalObject(){}
}

@SORMObject
class DoubleObject{
    @SORMID
    double id = 13.13;
    @SORMNoArgConstructor
    public DoubleObject(){}
}