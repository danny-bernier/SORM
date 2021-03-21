
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
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

    }
}
