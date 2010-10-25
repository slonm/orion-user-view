/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import orion.orionuserview.internal.EntityDefImpl;

/**
 *
 * @author sl
 */
public class RootRelationDefFactory {

    public static EntityDefImpl getFB() {
        String driver = "org.firebirdsql.jdbc.FBDriver";
        String url = "jdbc:firebirdsql:172.16.1.1/3050:zismg?lc_ctype=WIN1251";
        String username = "SYSDBA";
        String password = "ntktajy";
//        String rootTable = "Students";
        String rootTable = "Employees";
        EntityDefImpl rootRelDef = null;
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
//            rootRelDef = new EntityDefImpl(new DatabaseInfo(connection.getMetaData()), null, null, rootTable);
//            rootRelDef.getShownOnlyAsForegnKeyAttrPatterns().add("CODE");
//            rootRelDef.getUninterestedAttrPatterns().add("HASFILLED_NAME");
//            rootRelDef.getUninterestedAttrPatterns().add("HASFILLED_ROLE");
//            rootRelDef.addAdditionalReferenceBook(null, null, "City");
//            rootRelDef.addAdditionalReferenceBook(null, null, "TBoolean");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return rootRelDef;
    }

    public static EntityDefImpl getPGSQL() {
        String url = "jdbc:postgresql://localhost/cpu";
        String username = "postgres";
        String password = "postgres";
//        String schema = "pub";
//        String rootTable = "document";
        String schema = "uch";
        String rootTable = "license";
        EntityDefImpl rootRelDef = null;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
//            rootRelDef = new EntityDefImpl(new DatabaseInfo(connection.getMetaData()), null, schema, rootTable);
//            rootRelDef.getShownOnlyAsForegnKeyAttrPatterns().add("id");
//            rootRelDef.getUninterestedAttrPatterns().add("fill_date_time");
//            rootRelDef.getUninterestedAttrPatterns().add("modify_date_time");
//            rootRelDef.getUninterestedAttrPatterns().add("alias_to");
//            rootRelDef.getUninterestedAttrPatterns().add("is_obsolete");
//            rootRelDef.getUninterestedAttrPatterns().add("filler");
//            rootRelDef.getUninterestedAttrPatterns().add("key");
//            rootRelDef.addAdditionalReferenceBook(null, null, "knowlege_area_or_training_direction");
//            rootRelDef.addAdditionalReferenceBook(null, null, "training_direction_or_speciality");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return rootRelDef;
    }
}
