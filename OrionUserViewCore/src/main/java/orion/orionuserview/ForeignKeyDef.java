/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.orionuserview;

import java.util.Map;

/**
 *
 * @author sl
 */
public interface ForeignKeyDef extends RelationDef{
    Map<String, String> getPkOnFk();
    ForeignKeyType getForeignKeyType();
}
