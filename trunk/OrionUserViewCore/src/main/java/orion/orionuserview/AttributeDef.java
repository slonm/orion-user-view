package orion.orionuserview;

/**
 * Атрибут в общем смысле.
 * Атрибутом может быть:
 *  - скалярное поле таблицы (ScalarAttributeDef)
 *  - внешний ключ (ForeignKeyDef)
 *  - периодический атрибут (RelationDef)
 *  - группа атрибутов (GroupDef)
 * @author sl
 */
public interface AttributeDef {

    DatabaseDef getDatabaseDef();
    /**
     * Возвращает корневую сущность
     */
    EntityDef getRoot();

    /**
     * Возвращает отношение которому принадлежит атрибут
     */
    RelationDef getOwner();

    /**
     * используется при добавлении атрибута в колекцию relation
     */
    void setRelation(RelationDef relation);

    /**
     * Возвращает описание атрибута.
     * Если описания нет - возвращает null
     */
    String getRemarks();

    /**
     * Возвращает имя атрибута
     */
    String getName();
}
