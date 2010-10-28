package orion.orionuserview.internal;

import java.io.Serializable;
import orion.orionuserview.AttributeDef;
import orion.orionuserview.DatabaseDef;
import orion.orionuserview.EntityDef;
import orion.orionuserview.RelationDef;
import orion.orionuserview.utils.Defense;

/**
 * Атрибут в общем смысле.
 * Атрибутом может быть: 
 *  - скалярное поле таблицы (NuclearAttributeDef)
 *  - внешний ключ (ForeignKeyDef)
 *  - периодический атрибут (RelationDefImpl)
 *  - группа атрибутов (GroupDef)
 * @author sl
 */
public abstract class AttributeDefImpl implements AttributeDef, Serializable{

    private String name;
    private String remarks;
    private RelationDef relation;

    @Override
    public DatabaseDef getDatabaseDef() {
        return relation.getDatabaseDef();
    }

    /**
     * for XMLEncoder
     */
    public AttributeDefImpl() {
    }

    public AttributeDefImpl(String name, RelationDef relation) {
        this.name = Defense.notBlank(name, "name");
        this.relation = relation;
    }

    @Override
    public EntityDef getRoot() {
        return relation.getRoot();
    }

    @Override
    public RelationDef getOwner() {
        return relation;
    }

    @Override
    public void setRelation(RelationDef relation) {
        this.relation = relation;
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
        if (remarks != null && remarks.length() == 0) {
            remarks = null;
        }
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return remarks == null ? name : remarks;
    }

}
