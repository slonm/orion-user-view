/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview;

/**
 *
 * @author sl
 */
public interface Relation {

    String getCatalog();

    String getName();

    RelationSourceType getSourceType();

    String getRemarks();

    String getSchema();

    RelationType getRelationType();

    void setRelationType(RelationType relationType);
}
