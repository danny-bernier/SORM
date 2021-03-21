
import dev.database.DBConnection;
import dev.model.database.DataField;
import dev.model.enumeration.SQLDataType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Driver {
    public static void main(String[] args) throws Exception {
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
