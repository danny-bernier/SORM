
import dev.database.DBConnection;
import dev.database.SORMDAO;
import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.annotation.SORMReference;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Enumeration;

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

        @SORMObject
        class PotatoReference{
            @SORMID
            int babyCode = 31;
        }

        @SORMObject
        class Potato{
            @SORMID
            int spudID = 25;
            @SORMField
            String name = "Tony Potato";
            @SORMReference
            PotatoReference potatoReference = new PotatoReference();
        }

        SORMDAO<Potato, Integer> dao = new SORMDAO<>();
        Potato p = new Potato();
        dao.create(p);

        Connection con = DBConnection.getInstance().getConnection();
        System.out.println("3");
        PreparedStatement ps = con.prepareStatement("select 1 from Potato");
        System.out.println("4");
        ResultSet r = ps.executeQuery();
        System.out.println("5");
        System.out.println(r);

    }
}
