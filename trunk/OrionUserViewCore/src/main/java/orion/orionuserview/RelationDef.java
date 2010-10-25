/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sl
 */
public interface RelationDef extends AttributeDef, List<AttributeDef> {

    String getCatalog();

    String getSchema();

    RelationSourceType getRelationSourceType();

    boolean isInitiated();

    void init() throws SQLException;
}
