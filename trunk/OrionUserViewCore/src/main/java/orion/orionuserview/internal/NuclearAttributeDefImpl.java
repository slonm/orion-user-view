/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.orionuserview.internal;

import orion.orionuserview.NuclearAttributeDef;

/**
 *
 * @author sl
 */
public class NuclearAttributeDefImpl extends AttributeDefImpl implements NuclearAttributeDef{

    private Integer dataType;
    private boolean nullable=false;

    NuclearAttributeDefImpl(String name, RelationDefImpl relation, Integer dataType, boolean nullable) {
        super(name, relation);
        this.dataType = dataType;
        this.nullable = nullable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

}
