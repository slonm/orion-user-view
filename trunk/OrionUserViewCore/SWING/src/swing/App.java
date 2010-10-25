package swing;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MalformedURLException, SQLException
    {
//        String s1="jdbc:derby://localhost:1527/sample";
        String s1="jdbc:mysql://host/db";
        
        for(DriverPropertyInfo pi:DriverManager.getDriver(s1).getPropertyInfo(null, null)){
            System.out.println("Name:"+pi.name+" Value:"+pi.value+" Desc:"+pi.description+" Require:"+pi.required+" Choises:"+pi.choices);
        }
    }
    static void printURL(URL u){
        System.out.println(u.getHost());
        System.out.println(u.getAuthority());
        System.out.println(u.getDefaultPort());
        System.out.println(u.getPath());
        System.out.println(u.getProtocol());
        System.out.println(u.getQuery());
        System.out.println(u.getRef());
        System.out.println(u.getUserInfo());
        System.out.println(u.toExternalForm());
        System.out.println(u.toString());
    }
}
