package orion.orionuserview.test;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import orion.orionuserview.internal.DatabaseDefImpl;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws SQLException, FileNotFoundException, ClassNotFoundException {
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        DatabaseDefImpl dd = new DatabaseDefImpl("jdbc:firebirdsql:172.16.1.1/3050:zismg?lc_ctype=WIN1251", "SYSDBA", "ntktajy");
        dd.getHiddenAttrPatterns().add("CODE");
        dd.getHiddenAttrPatterns().add("HASFILLED_NAME");
        dd.getHiddenAttrPatterns().add("HASFILLED_ROLE");
        dd.getRootRelationDefs();
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Beanarchive.xml")));
        encoder.writeObject(dd);
        encoder.close();
    }
}
