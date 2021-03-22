package dev.model.database;

import dev.database.DBConnection;
import dev.database.SORMDAO;
import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.annotation.SORMReference;
import org.junit.Assert;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SORMDAOTest {

    @Test
    public void SORMDAOCreatingCascadingTablesWithReferences() {
        @SORMObject
        class PotatoReference{
            @SORMID
            double babyCode = 31.522;
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

        boolean result = false;

        try {
            SORMDAO<Potato, Integer> dao = new SORMDAO<>();
            Potato p = new Potato();
            result = dao.create(p);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

        try (Connection con = DBConnection.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from PotatoReference");
            ResultSet r = ps.executeQuery();
            r.next();
            Assert.assertEquals(31.522, r.getObject(1));
            ps = con.prepareStatement("select * from Potato");
            r = ps.executeQuery();
            r.next();
            Assert.assertEquals(25, r.getObject(1));
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertTrue(result);
    }
}
