//                       TABLE_CAT|                     TABLE_SCHEM|                      TABLE_NAME|                      TABLE_TYPE|                         REMARKS|                      OWNER_NAME|
package orion.orionuserview.internal;

import orion.orionuserview.Relation;
import orion.orionuserview.RelationSourceType;
import orion.orionuserview.RelationType;

/**
 *
 * @author sl
 */
public class RelationImpl implements Relation {

    public String catalog;
    public String schema;
    public String name;
    public String remarks;
    public RelationSourceType sourceType = RelationSourceType.TABLE;
    public RelationType relationType = RelationType.UNKNOWN;

    public RelationImpl() {
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public RelationSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(RelationSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public RelationImpl(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    @Override
    public String toString() {
        return (catalog != null ? catalog + "." : "")
                + (schema != null ? schema + "." : "")
                + name;
    }

    @Override
    public RelationType getRelationType() {
        return relationType;
    }

    @Override
    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelationImpl other = (RelationImpl) obj;
        if ((this.catalog == null) ? (other.catalog != null) : !this.catalog.equals(other.catalog)) {
            return false;
        }
        if ((this.schema == null) ? (other.schema != null) : !this.schema.equals(other.schema)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.catalog != null ? this.catalog.hashCode() : 0);
        hash = 29 * hash + (this.schema != null ? this.schema.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
