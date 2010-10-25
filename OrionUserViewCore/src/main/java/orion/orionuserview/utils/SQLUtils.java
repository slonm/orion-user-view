/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.orionuserview.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sl
 */
public class SQLUtils {
    public static String toIdentifier(DatabaseMetaData metaData, String identifier) throws SQLException{
        return identifier;
        //if(identifier==null||identifier.length()==0)return null;
        //return metaData.getIdentifierQuoteString()+identifier+metaData.getIdentifierQuoteString();
    }

    public static String toNullString(ResultSet rs, String field) throws SQLException {
        if (rs.getObject(field) != null) {
            return rs.getString(field);
        }
        return null;
    }

}
