package orion.orionuserview.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Удаление всех таблиц базы данных
 * @author sl
 */
public class TestApp {
    public static Connection getJDBCConnection(String driverName, String url, String userid, String password) {
        Connection con = null;
        try {
            Class.forName(driverName);
        } catch (java.lang.ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (java.lang.NoClassDefFoundError e) {
            System.err.println(e.getMessage());
        }

        try {
            con = DriverManager.getConnection(url, userid, password);
            System.out.println("Got Connection to " + url);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        return con;
    }

// Display an SQLException which has occured in this application.
    private static void showSQLException(java.sql.SQLException e) {
// Notice that a SQLException is actually a chain of SQLExceptions,
// let's not forget to print all of them...
        java.sql.SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }

    public static void main(String[] args) throws Exception {
        Connection pgCon = getJDBCConnection("org.postgresql.Driver",
                "jdbc:postgresql://localhost/cpu",
                "postgres", "postgres");
        Connection fbCon = getJDBCConnection("org.firebirdsql.jdbc.FBDriver",
                "jdbc:firebirdsql://172.16.1.1:3050/zismg?lc_ctype=WIN1251",
//                "jdbc:firebirdsql:172.16.1.1/3050:zismg_old?lc_ctype=WIN1251",
                "SYSDBA", "ntktajy");
        if (pgCon != null) {
//            System.out.println("postgres ExtraNameCharacters: "+ pgCon.getMetaData().getExtraNameCharacters());
//            System.out.println("postgres IdentifierQuoteString: "+ pgCon.getMetaData().getIdentifierQuoteString());
//            printResultSet("postgres schemas",pgCon.getMetaData().getSchemas());
//            printResultSet("postgres tables",pgCon.getMetaData().getTables(null, null, null, new String[]{"TABLE","VIEW"}));
//            printResultSet("postgres columns",pgCon.getMetaData().getColumns(null, null, "education_form", null));
//            printResultSet("postgres fk",pgCon.getMetaData().getCrossReference(null, null, "training_direction_or_speciality", null, null, "knowledge_area_or_training_direction"));
//            printResultSet("postgres fk",pgCon.getMetaData().getCrossReference(null, null, "knowledge_area_or_training_direction", null, null, "training_direction_or_speciality"));
//            printResultSet("postgres fk",pgCon.getMetaData().getCrossReference(null, null, "training_direction_or_speciality", null, null, null));
//            printResultSet("postgres fk",pgCon.getMetaData().getCrossReference(null, null, null, null, null, "training_direction_or_speciality"));
//            printResultSet("postgres unique index",pgCon.getMetaData().getIndexInfo(null, null, null, true, false));

            pgCon.close();
        }
        if (fbCon != null) {
//            System.out.println("firebird ExtraNameCharacters: "+ fbCon.getMetaData().getExtraNameCharacters());
//            System.out.println("firebird IdentifierQuoteString: "+ fbCon.getMetaData().getIdentifierQuoteString());
//            printResultSet("firebird catalogs",fbCon.getMetaData().getCatalogs());
//            printResultSet("firebird schemas",fbCon.getMetaData().getSchemas(null, null));
//            printResultSet("firebird tables",fbCon.getMetaData().getTables(null, null, null, new String[]{"TABLE","VIEW"}));
//            printResultSet("firebird columns",fbCon.getMetaData().getColumns(null, null, "Military_Registration", null));
            printResultSet("firebird fk",fbCon.getMetaData().getCrossReference(null, null, null, null, null, "\"Employees\""));
//            printResultSet("firebird unique index",fbCon.getMetaData().getIndexInfo(null, null, "Structure_Family", true, true));
//            printResultSet("firebird primary keys",fbCon.getMetaData().getPrimaryKeys(null, null, "Structure_Family"));
            fbCon.close();
        }
    }

    private static void printResultSet(String header, ResultSet rs) {
        System.out.println("### ROW_SET_HEADER: "+header+" ###");
        Integer colWidth = 32;
        try {
            String headerRow = "";
            String delimiterRow = "";
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                headerRow += String.format("%" + colWidth + "s|", rsMetaData.getColumnName(i));
                for (int j = 0; j < colWidth; j++) {
                    delimiterRow += "-";
                }
                delimiterRow += "|";
            }
            System.out.println(headerRow);
            System.out.println(delimiterRow);
            while (rs.next()) {
                String row = "";
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    row += String.format("%" + colWidth + "s|", rs.getString(i));
                }
                System.out.println(row);
            }
        } catch (SQLException ex) {
            showSQLException(ex);
        }

    }
}
