/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview;

import java.util.Set;

/**
 *
 * @author sl
 */
public interface EntityDef extends RelationDef {

    Set<Relation> getAdditionalReferenceBooks();
}
