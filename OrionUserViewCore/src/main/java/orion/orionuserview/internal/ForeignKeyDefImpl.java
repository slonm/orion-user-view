package orion.orionuserview.internal;

import java.util.*;
import orion.orionuserview.ForeignKeyDef;
import orion.orionuserview.ForeignKeyType;
import orion.orionuserview.Relation;
import orion.orionuserview.RelationDef;

/**
 * Таблица на которую ссылается внешний ключ и описание внешнего ключа.
 * @author sl
 */
public class ForeignKeyDefImpl extends RelationDefImpl implements ForeignKeyDef{

    private CrossReference crossReference;
    private ForeignKeyType foregnKeyType;
    private String alias;

    /**
     * for XMLEncoder
     */
    public ForeignKeyDefImpl() {
    }

    ForeignKeyDefImpl(Relation relation, RelationDef relationDef,
            EntityDefImpl rootRelation, ForeignKeyType foregnKeyType,
            CrossReference cr) {
        super(relation, relationDef, rootRelation);
        this.foregnKeyType = foregnKeyType;
        this.crossReference=cr;
    }

    /**
     * На самом деле левая часть тип связи на стороне родительской таблицы,
     * а правая - на стороне этой таблицы
     */
    @Override
    public ForeignKeyType getForeignKeyType() {
        return foregnKeyType;
    }

    public void setForegnKeyType(ForeignKeyType foregnKeyType) {
        this.foregnKeyType = foregnKeyType;
    }

    /**
     * Пары полей <primaryKeyAttribute, foregnKeyAttribute> участвующих во внешнем ключе
     * независимо от того какая таблица главная, а какая дочерняя
     */
    @Override
    public Map<String, String> getPkOnFk() {
        return crossReference.pkOnFk;
    }

    public CrossReference getCrossReference() {
        return crossReference;
    }

    @Override
    public String getAlias() {
        if(alias==null){
            this.alias = getDatabaseDef().newAlias(this);
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
