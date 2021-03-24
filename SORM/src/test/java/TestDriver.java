
import dev.database.SORMDAO;
import dev.model.annotation.*;
import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.utility.reflection.POJOPropertyGetSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class TestDriver {
    public static void main(String[] args) throws Exception {
        //-------------Testing h2 some more---------------------------------
//        Class.forName("org.h2.Driver");
//        Connection con = DriverManager.getConnection("jdbc:h2:mem:tcp://localhost/~/test","sa","");
        //-------------------------------------------------------------------------------------------------------

        //Connection con = DriverManager.getConnection("jdbc:h2:~/test","sa","");
//        System.out.println(SQLDataType.INVALID.convertToSQLDataType(true));
        //System.out.println(DataField.createDataField(5));

        //-----Testing that i can connect to h2 using my xml creadentials and DBConnection---------------------
//        Connection con = DBConnection.getInstance().getConnection();
//        PreparedStatement ps = con.prepareStatement("create table f(id Integer)");
//        ps.executeUpdate();
//        ps = con.prepareStatement("insert into f values (3)");
//        ps.executeUpdate();
//        ps = con.prepareStatement("select * from f");
//        ResultSet r = ps.executeQuery();
//        while(r.next())
//            System.out.println(r.getInt("id"));
//        System.out.println(con);
        //----------------------------------------------------------------------------------------

        //-------Testing to check if a table exists-----------------
//        Connection con = DBConnection.getInstance().getConnection();
//        PreparedStatement ps = con.prepareStatement("create table f(id Integer)");
//        ps.executeUpdate();
//        ps = con.prepareStatement("insert into f values (3)");
//        ps.executeUpdate();
//        ps = con.prepareStatement("select * from f");
//        ResultSet r = ps.executeQuery();
//        while(r.next())
//            System.out.println(r.getInt("id"));
//        System.out.println(con);
//        try {
//            ps = con.prepareStatement("select 1 from foo");
//            ps.executeQuery();
//            System.out.println("its real");
//        } catch (SQLException ignored){
//            System.out.println("its not there");
//        }
//        try {
//            ps = con.prepareStatement("select 1 from f");
//            ps.executeQuery();
//            System.out.println("table already exists");
//        } catch (SQLException ignored){
//            try {
//                ps = con.prepareStatement("create table f(name Text)");
//                ps.executeUpdate();
//                System.out.println("its a new table");
//            } catch (SQLException e){
//                e.printStackTrace();
//                System.out.println("Table did not exist but I couldnt make it :(");
//            }
//        }
        //------------------------------------------------------------------------------------------------

//        @SORMObject
//        class PotatoReference implements Serializable {
//            @SORMID
//            int babyCode = 31;
//        }
//
//        @SORMObject
//        class Potato implements Serializable {
//            @SORMID
//            int spudID = 25;
//            @SORMField
//            String name;
//            @SORMReference
//            PotatoReference potatoReference = new PotatoReference();
//            public Potato(String name){
//                this.name = name;
//            }
//        }
//
//        SORMDAO<Potato, Integer> dao = new SORMDAO<>(Potato.class, Integer.class);
//        Potato p = new Potato("Tony");
//        dao.create(p);
//        Optional<Potato> po = dao.getById(p.spudID);
//        if(po.isPresent())
//            System.out.println(po.get().name);
//        else
//            System.out.println("empty optional");
//        Potato p3 = new Potato("Billy");
//        dao.update(p3);
//        Optional<Potato> p3o = dao.getById(p.spudID);
//        if(p3o.isPresent())
//            System.out.println(p3o.get().name);
//        else
//            System.out.println("empty optional");
//        dao.delete(p3);
//        p3o = dao.getById(p.spudID);
//        if(p3o.isPresent())
//            System.out.println(p3o.get().name);
//        else
//            System.out.println("empty optional");

        //-----------------------------------------------------------

//        Connection con = DBConnection.getInstance().getConnection();
//        System.out.println("3");
//        PreparedStatement ps = con.prepareStatement("select * from Potato");
//        System.out.println("4");
//        ResultSet r = ps.executeQuery();
//        System.out.println("5");
//        System.out.println(r);
//
//        r.next();
//        //retrieveing and rebuilding object
//        byte[] buf = r.getBytes(4);
//        ObjectInputStream objectIn = null;
//        if (buf != null)
//            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
//
//        assert objectIn != null;
//        Object deSerializedObject = objectIn.readObject();
//        Potato ne = (Potato)deSerializedObject;
//        System.out.println(ne.name);
        //------------------------------------------------------------------
        //---------------------Reconstructing POJO with reflections--------------
//        Class<User> clazz = User.class;
//        Constructor[] constructors = clazz.getDeclaredConstructors();
//        Constructor noArg;
//        for (Constructor c:constructors) {
//            if(c.isAnnotationPresent(SORMNoArgConstructor.class)) {
//                c.setAccessible(true);
//                noArg = c;
//                User object = (User) noArg.newInstance();
//
//                System.out.println(object.toString());
//
//                DataField<?> id = POJOPropertyGetSet.getID(object);
//                List<DataField<Object>> fields = POJOPropertyGetSet.getFields(object);
//                List<DataReference<Object>> references = POJOPropertyGetSet.getReference(object);
//                Field idField = clazz.getDeclaredField(id.getValueFieldName());
//                idField.setAccessible(true);
//                idField.set(object, 155);
//
//                for (DataField<Object> f:fields) {
//                    Field field = clazz.getDeclaredField(f.getValueFieldName());
//                    field.setAccessible(true);
//                    field.set(object, "TEST STRING");
//                }
//
//                System.out.println(object.toString());
//            }
//        }
        //---------------------------------------------------------------------------
        User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
        SORMDAO<User, Integer> dao = new SORMDAO<>(User.class, Integer.class);
        System.out.println(dao.create(u));
        Optional<User> ou = dao.getById(23);
        System.out.println(ou.isPresent());
        System.out.println(u = ou.get());
        System.out.println(u.myCar);
    }
}

@SORMObject
class User{
    @SORMID
    private int id;
    @SORMField
    private String name = "";
    @SORMField
    private String email = "";
    @SORMReference
    public Car myCar = new Car("Ford", "Focus",  83774561);

    @SORMNoArgConstructor
    private User(){}

    public User(String name, String email, int id, Car car){
        this.name = name;
        this.email = email;
        this.id = id;
        this.myCar = car;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

@SORMObject
class Car{
    @SORMID
    private int vin;
    @SORMField
    private String make = "";
    @SORMField
    private String model = "";

    @SORMNoArgConstructor
    private Car(){}

    public Car(String make, String model, int vin){
        this.make = make;
        this.model = model;
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Car{" +
                "vin=" + vin +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}