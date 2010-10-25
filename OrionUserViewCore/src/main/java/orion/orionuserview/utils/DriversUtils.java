/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.utils;

import java.sql.DriverManager;
import java.util.*;

/**
 *
 * @author sl
 */
public class DriversUtils {

    private static final Map<String, DriverDef> driverDefs = new HashMap<String, DriverDef>();
    private static Set<String> availableDriverNames;

    public static void add(String name, String className, String urlFormat) {
        driverDefs.put(name, new DriverDef(name, className, urlFormat));
    }

    public static void add(String name, String type, String className, String urlFormat) {
        driverDefs.put(name, new DriverDef(name + " " + type, className, urlFormat));
    }

    public static Set<String> getAvailableDriverNames() {
        if (availableDriverNames == null) {
            availableDriverNames = new HashSet<String>();
            //попытаемся загрузить все драйверы,
            //что-бы они зарегистировались в DriverManager
            for (DriverDef dd : driverDefs.values()) {
                try {
                    Class.forName(dd.getClassName());
                    availableDriverNames.add(dd.getName());
                    dd.setDriver(DriverManager.getDriver(dd.getURLFormat()));
                } catch (Throwable t) {
                }
            }
        }
        return availableDriverNames;
    }

    public static Map<String, DriverDef> getDriverDefs() {
        return Collections.unmodifiableMap(driverDefs);
    }
//TODO Вынести этот список в XML

    static {
        add("IBM DB2 (net)",
                "COM.ibm.db2.jdbc.net.DB2Driver",
                "jdbc:db2://<HOST>:<PORT>/<DB>");

        add("IBM DB2 (local)",
                "COM.ibm.db2.jdbc.app.DB2Driver",
                "jdbc:db2:<DB>");

        add("JDBC-ODBC Bridge",
                "sun.jdbc.odbc.JdbcOdbcDriver",
                "jdbc:odbc:<DB>");

        add("Microsoft SQL Server (Weblogic driver)",
                "weblogic.jdbc.mssqlserver4.Driver",
                "jdbc:weblogic:mssqlserver4:<DB>@<HOST>[:<PORT>]");

        add("PointBase", "Network Server",
                "com.pointbase.jdbc.jdbcUniversalDriver",
                "jdbc:pointbase://<HOST>[:<PORT>]/<DB>");

        add("PointBase", "Embedded",
                "com.pointbase.jdbc.jdbcUniversalDriver",
                "jdbc:pointbase://embedded[:<PORT>]/<DB>");

        add("PointBase", "Mobile Edition",
                "com.pointbase.jdbc.jdbcUniversalDriver",
                "jdbc:pointbase:<DB>");

        add("Cloudscape",
                "COM.cloudscape.core.JDBCDriver",
                "jdbc:cloudscape:<DB>");

        add("Cloudscape RMI",
                "RmiJdbc.RJDriver",
                "jdbc:rmi://<HOST>[:<PORT>]/jdbc:cloudscape:<DB>");

        add("Java DB (Embedded)",
                "org.apache.derby.jdbc.EmbeddedDriver",
                "jdbc:derby:<DB>[;<ADDITIONAL>]");

        add("Java DB (Network)",
                "org.apache.derby.jdbc.ClientDriver",
                "jdbc:derby://<HOST>[:<PORT>]/<DB>[;<ADDITIONAL>]");

        add("IBM DB2 Universal Driver",
                "com.ibm.db2.jcc.DB2Driver",
                "jdbc:db2://<HOST>:<PORT>/<DB>[:<ADDITIONAL>]");

        add("IBM DB2 Universal Driver Informix Dynamic Server (IDS)",
                "com.ibm.db2.jcc.DB2Driver",
                "jdbc:ids://<HOST>:<PORT>/<DB>[:<ADDITIONAL>]");

        add("IBM DB2 Universal Driver Cloudscape Server",
                "com.ibm.db2.jcc.DB2Driver",
                "jdbc:db2j:net://<HOST>:<PORT>/<DB>[:<ADDITIONAL>]");

        add("Firebird (JCA/JDBC driver)",
                "org.firebirdsql.jdbc.FBDriver",
                "jdbc:firebirdsql:[//<HOST>[:<PORT>]/]<DB>");

        add("FirstSQL/J", "Enterprise Server Edition",
                "COM.FirstSQL.Dbcp.DbcpDriver",
                "jdbc:dbcp://<HOST>[:<PORT>]");

        add("FirstSQL/J", "Professional Edition",
                "COM.FirstSQL.Dbcp.DbcpDriver",
                "jdbc:dbcp://local");

        add("IBM DB2 (DataDirect Connect for JDBC)",
                "com.ddtek.jdbc.db2.DB2Driver",
                "jdbc:datadirect:db2://<HOST>[:<PORT>][;databaseName=<DB>]");

        add("IDS Server",
                "ids.sql.IDSDriver",
                "jdbc:ids://<HOST>[:<PORT>]/conn?dsn='<DSN>'");

        add("Informix Dynamic Server",
                "com.informix.jdbc.IfxDriver",
                "jdbc:informix-sqli://<HOST>[:<PORT>]/<DB>:INFORMIXSERVER=<SERVER_NAME>");

        add("Informix Dynamic Server (DataDirect Connect for JDBC)",
                "com.ddtek.jdbc.informix.InformixDriver",
                "jdbc:datadirect:informix://<HOST>[:<PORT>];informixServer=<SERVER_NAME>;databaseName=<DB>");

        add("InstantDB (v3.13 and earlier)",
                "jdbc.idbDriver",
                "jdbc:idb:<DB>");

        add("InstantDB (v3.14 and later)",
                "org.enhydra.instantdb.jdbc.idbDriver",
                "jdbc:idb:<DB>");

        add("Interbase (InterClient driver)",
                "interbase.interclient.Driver",
                "jdbc:interbase://<HOST>/<DB>");

        add("HSQLDB", "Server",
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:hsql://<HOST>[:<PORT>]");

        add("HSQLDB", "Embedded",
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:<DB>");

        add("HSQLDB", "Web Server",
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:http://<HOST>[:<PORT>]");

        add("HSQLDB", "In-Memory",
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:.");

        add("Hypersonic SQL (v1.2 and earlier)",
                "hSql.hDriver",
                "jdbc:HypersonicSQL:<DB>");

        add("Hypersonic SQL (v1.3 and later)",
                "org.hsql.jdbcDriver",
                "jdbc:HypersonicSQL:<DB>");

        add("jTDS for Microsoft SQL Server",
                "net.sourceforge.jtds.jdbc.Driver",
                "jdbc:jtds:sqlserver://<HOST>[:<PORT>][/<DB>][;<ADDITIONAL>]");

        add("jTDS for Sybase ASE",
                "net.sourceforge.jtds.jdbc.Driver",
                "jdbc:jtds:sybase://<HOST>[:<PORT>][/<DB>][;<ADDITIONAL>]");

        add("Mckoi SQL Database", "Server",
                "com.mckoi.JDBCDriver",
                "jdbc:mckoi://<HOST>[:<PORT>]");

        add("Mckoi SQL Database", "Embedded",
                "com.mckoi.JDBCDriver",
                "jdbc:mckoi:local://<DB>");

        add("Microsoft SQL Server (DataDirect Connect for JDBC)",
                "com.ddtek.jdbc.sqlserver.SQLServerDriver",
                "jdbc:datadirect:sqlserver://<HOST>[:<PORT>][;databaseName=<DB>]");

        add("Microsoft SQL Server (JTurbo driver)",
                "com.ashna.jturbo.driver.Driver",
                "jdbc:JTurbo://<HOST>:<PORT>/<DB>");

        add("Microsoft SQL Server (Sprinta driver)",
                "com.inet.tds.TdsDriver",
                "jdbc:inetdae:<HOST>[:<PORT>]?database=<DB>");

        add("Microsoft SQL Server 2000 (Microsoft driver)",
                "com.microsoft.jdbc.sqlserver.SQLServerDriver",
                "jdbc:microsoft:sqlserver://<HOST>[:<PORT>][;DatabaseName=<DB>]");

        add("Microsoft SQL Server 2005",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://[<HOST>[\\<INSTANCE>][:<PORT>]][;databaseName=<DB>][;<ADDITIONAL>]");

        add("MySQL (Connector/J driver)",
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://[<HOST>[:<PORT>]][/<DB>][?<ADDITIONAL>]"); // NOI18N

        add("MySQL (MM.MySQL driver)",
                "org.gjt.mm.mysql.Driver",
                "jdbc:mysql://<HOST>[:<PORT>]/<DB>");

        add("Oracle Thin with Service ID (SID)",
                "oracle.jdbc.OracleDriver",
                "jdbc:oracle:thin:@<HOST>:<PORT>:<SID>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle Thin with Service Name",
                "oracle.jdbc.OracleDriver",
                "jdbc:oracle:thin:@//<HOST>:<PORT>/<SERVICE>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle Thin with TNS Name (v10.2.0.1.0 or later)",
                "oracle.jdbc.OracleDriver",
                "jdbc:oracle:thin:@<TNSNAME>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle OCI OCI8 with Service ID (SID)",
                "oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:oci8:@<HOST>:<PORT>:<SID>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle OCI OCI8 with Service Name",
                "oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:oci8:@//<HOST>:<PORT>/<SERVICE>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle OCI with TNS Name (v10.2.0.1.0 or later)",
                "oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:oci:@<TNSNAME>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle OCI with Service ID (SID)",
                "oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:oci:@<HOST>:<PORT>:<SID>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle OCI with Service Name",
                "oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:oci:@//<HOST>:<PORT>/<SERVICE>[?<ADDITIONAL>]"); // NOI18N

        add("Oracle (DataDirect Connect for JDBC)",
                "com.ddtek.jdbc.oracle.OracleDriver",
                "jdbc:datadirect:oracle://<HOST>[:<PORT>];SID=<SID>");

        add("PostgreSQL (v6.5 and earlier)",
                "postgresql.Driver",
                "jdbc:postgresql:[//<HOST>[:<PORT>]/]<DB>[?<ADDITIONAL>]");

        add("PostgreSQL", // 7.0 and later
                "org.postgresql.Driver",
                "jdbc:postgresql:[//<HOST>[:<PORT>]/]<DB>[?<ADDITIONAL>]");

        add("Quadcap Embeddable Database",
                "com.quadcap.jdbc.JdbcDriver",
                "jdbc:qed:<DB>");

        add("Sybase (jConnect 4.2 and earlier)",
                "com.sybase.jdbc.SybDriver",
                "jdbc:sybase:Tds:<HOST>[:<PORT>]");

        add("Sybase (jConnect 5.2)",
                "com.sybase.jdbc2.jdbc.SybDriver",
                "jdbc:sybase:Tds:<HOST>[:<PORT>]");

        add("Sybase (DataDirect Connect for JDBC)",
                "com.ddtek.jdbc.sybase.SybaseDriver",
                "jdbc:datadirect:sybase://<HOST>[:<PORT>][;databaseName=<DB>]");

        // Following four entries for drivers to be included in Java Studio Enterprise 7 (Bow)
        add("Microsoft SQL Server Driver",
                "com.sun.sql.jdbc.sqlserver.SQLServerDriver",
                "jdbc:sun:sqlserver://<HOST>[:<PORT>]");

        add("DB2 Driver",
                "com.sun.sql.jdbc.db2.DB2Driver",
                "jdbc:sun:db2://<HOST>[:<PORT>];databaseName=<DB>");

        add("Oracle Driver",
                "com.sun.sql.jdbc.oracle.OracleDriver",
                "jdbc:sun:oracle://<HOST>[:<PORT>][;SID=<SID>]");

        add("Sybase Driver",
                "com.sun.sql.jdbc.sybase.SybaseDriver",
                "jdbc:sun:sybase://<HOST>[:<PORT]");

    }
}
