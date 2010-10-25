package orion.orionuserview.internal;

import java.util.*;
import java.util.ListIterator;
import java.sql.SQLException;
import orion.orionuserview.*;
import orion.orionuserview.utils.Defense;

/**
 * Отношение. Реализует List<AttributeDefImpl> для того, что-бы при добавлении
 * атрибутов присвоить у них поле relation в this
 * 
 * @author sl
 */
public class RelationDefImpl extends AttributeDefImpl implements RelationDef {

    private Relation relation;
    private List<AttributeDef> attributes = new ArrayList<AttributeDef>();
    private RelationSourceType relationSourceType;
    transient private boolean initiated = false;
    private EntityDefImpl rootEntityDefImpl;
    private Set<UniqueIndex> uniqueIndexes;

    /**
     * for XMLEncoder
     */
    public RelationDefImpl() {
    }

    RelationDefImpl(Relation relation,
            RelationDef relationDef,
            EntityDefImpl rootEntityDefImpl) {
        super(relation.getName(), relationDef);
        this.relation = relation;
        this.setRemarks(relation.getRemarks());
        this.relationSourceType = relation.getSourceType();
        this.rootEntityDefImpl = rootEntityDefImpl;
    }

    /**
     * Делаем инициализацию по требованию для избежания инициализации атрибутов - таблиц
     * @throws SQLException
     */
    @Override
    public void init() throws SQLException {
        if (!initiated) {
            MetadataCache metadataCache = rootEntityDefImpl.getDatabaseDefImpl().getMetadataCache();
            //Может uniqueIndexes уже заполнено родительским отношением, тогда оставим его
            if (uniqueIndexes == null) {
                uniqueIndexes = metadataCache.getUniqueIndexes(relation);
            }
            //------------Добавляем скалярные поля-------------
            List<Column> columns = metadataCache.getColumns(relation);
            for (Column column : columns) {
                boolean hidden = false;
                //------------удалим поля из hiddenAttrPatterns------------------
                for (String pattern : getRoot().getDatabaseDef().getHiddenAttrPatterns()) {
                    if (column.name.matches(String.format(pattern, getName()))) {
                        hidden = true;
                        break;
                    }
                }
                if (!hidden) {
                    NuclearAttributeDefImpl ad = new NuclearAttributeDefImpl(column.name,
                            this, column.type, column.nullable);
                    ad.setRemarks(column.remarks);
                    add(ad);
                }
            }
            //------------Заменим поля связи на атрибут - внешний ключ
            replaceNuclearAttributeByForeignKey();
            //------------Добавим группы и периодические атрибуты
            addDependent();
            initiated = true;
        }
    }

    private void replaceNuclearAttributeByForeignKey() throws SQLException {
        //---------------Добавляем внешние ключи ManyTo(One|Many)----------------------
        MetadataCache metadataCache = rootEntityDefImpl.getDatabaseDefImpl().getMetadataCache();
        Set<CrossReference> crossReferences = metadataCache.getCrossReferences(null, relation);
        //Создадим и сформируем объекты ForeignKeyDefImpl
        for (CrossReference crRef : crossReferences) {
            if (crRef.pkRelation.getRelationType() != RelationType.HIDDEN) {
                ForeignKeyType fkType = ForeignKeyType.MANY_TO_ONE;
                //Выясним тип связи. Если в текущей таблице есть unique с полями crRef.pkOnFk.keySet()
                //то это ONE_TO_ONE_PK, иначе MANY_TO_ONE
                for (UniqueIndex ui : uniqueIndexes) {
                    if (ui.columns.equals(new HashSet(crRef.pkOnFk.values()))) {
                        fkType = ForeignKeyType.ONE_TO_ONE_PK;
                        break;
                    }
                }

                ForeignKeyDefImpl fkd = new ForeignKeyDefImpl(crRef.pkRelation, this, getRootEntityDefImpl(),
                        fkType, crRef);
                boolean moved = false;
                for (String fkColumnName : fkd.getPkOnFk().values()) {
                    ListIterator<AttributeDef> thisIter = this.listIterator();
                    while (thisIter.hasNext()) {
                        AttributeDef ad = thisIter.next();
                        if (ad instanceof NuclearAttributeDefImpl
                                && ad.getName().equals(fkColumnName)) {
                            //Заменяем первое поле входящее во внешний ключ на объект ForeignKeyDefImpl.
                            //Поместим fkd на свое место в ret если это еще не сделано
                            if (!moved) {
                                //В случае ForeignKeyType.ONE_TO_ONE_PK описание не заполняем
                                if (fkd.getForeignKeyType() != ForeignKeyType.ONE_TO_ONE_PK) {
                                    fkd.setRemarks(ad.getRemarks());
                                }
                                //исключим циклические ссылки
                                if (haveForeignKeyInParents(this, crRef)) {
                                    thisIter.remove();
                                } else {
                                    thisIter.set(fkd);
                                }
                                moved = true;
                            } else {
                                //В случае ForeignKeyType.ONE_TO_ONE_PK описание не заполняем
                                if (fkd.getForeignKeyType() != ForeignKeyType.ONE_TO_ONE_PK) {
                                    fkd.setRemarks((fkd.getRemarks() != null ? fkd.getRemarks() + " | " : "") + (ad.getRemarks() != null ? ad.getRemarks() : ad.getName()));
                                }
                                //Если во внешнем ключе участвуют несколько полей, то остальные поля удаляем
                                thisIter.remove();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addDependent() throws SQLException {
        //---------------Добавляем внешние ключи OneTo(One|Many)----------------------
        MetadataCache metadataCache = rootEntityDefImpl.getDatabaseDefImpl().getMetadataCache();
        if (relation.getRelationType() != RelationType.REFERENCE_BOOK
                && !getRoot().getAdditionalReferenceBooks().contains(relation)) {
            //Закешируем ResultSet в множество для удобства работы
            Set<CrossReference> crossReferences = metadataCache.getCrossReferences(relation, null);
            //Множество уде добавленных зависимых таблиц
            Set<Relation> addedRelations = new HashSet<Relation>();
            //Создадим и сформируем объекты ForeignKeyDefImpl
            for (CrossReference crRef : crossReferences) {
                //Исключим повторы зависимых таблиц. Они могут быть если разные
                //поля зависимой таблицы ссылаются на родительскую таблицу
                //FIXME добавляется ссылка на первый попавшийся атрибут
                if (!addedRelations.contains(crRef.fkRelation)) {
                    //исключим циклические ссылки
                    if (crRef.fkRelation.getRelationType() != RelationType.HIDDEN
                            && !haveForeignKeyInParents(this, crRef)) {
                        ForeignKeyDefImpl fkd = new ForeignKeyDefImpl(crRef.fkRelation, this, getRootEntityDefImpl(),
                                ForeignKeyType.ONE_TO_MANY, crRef);
                        //Проверим - может это OneToOne
                        fkd.setUniqueIndexes(metadataCache.getUniqueIndexes(fkd.getRelation()));
                        for (UniqueIndex ui : fkd.getUniqueIndexes()) {
                            if (ui.columns.equals(new HashSet(fkd.getPkOnFk().values()))) {
                                fkd.setForegnKeyType(ForeignKeyType.ONE_TO_ONE_FK);
                                break;
                            }
                        }
                        //Добавляем в конец списка полей
                        add(fkd);
                    }
                    addedRelations.add(crRef.fkRelation);
                }
            }
        }
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    @Override
    public EntityDef getRoot() {
        return rootEntityDefImpl;
    }

    @Override
    public DatabaseDef getDatabaseDef() {
        return rootEntityDefImpl.getDatabaseDef();
    }

    public EntityDefImpl getRootEntityDefImpl() {
        return rootEntityDefImpl;
    }

    public void setRootEntityDefImpl(EntityDefImpl rootEntityDefImpl) {
        this.rootEntityDefImpl = rootEntityDefImpl;
    }

    public Set<UniqueIndex> getUniqueIndexes() {
        return uniqueIndexes;
    }

    void setUniqueIndexes(Set<UniqueIndex> uniqueIndexes) {
        this.uniqueIndexes = uniqueIndexes;
    }

    @Override
    public boolean isInitiated() {
        return initiated;
    }

    @Override
    public RelationSourceType getRelationSourceType() {
        return relationSourceType;
    }

    public void setRelationSourceType(RelationSourceType relationSourceType) {
        this.relationSourceType = relationSourceType;
    }

    @Override
    public String getCatalog() {
        return relation.getCatalog();
    }

    @Override
    public String getSchema() {
        return relation.getSchema();
    }

    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public AttributeDef set(int index, AttributeDef element) {
        Defense.notNull(element, "element");
        AttributeDef ret = attributes.set(index, element);
        element.setRelation(this);
        return ret;
    }

    @Override
    public AttributeDef remove(int index) {
        return attributes.remove(index);
    }

    public boolean remove(AttributeDefImpl o) {
        return attributes.remove(o);
    }

    @Override
    public Iterator<AttributeDef> iterator() {
        return attributes.iterator();
    }

    @Override
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public int indexOf(Object o) {
        return attributes.indexOf(o);
    }

    @Override
    public AttributeDef get(int index) {
        return attributes.get(index);
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean contains(Object o) {
        return attributes.contains(o);
    }

    @Override
    public void clear() {
        attributes.clear();
    }

    @Override
    public void add(int index, AttributeDef element) {
        Defense.notNull(element, "element");
        attributes.add(index, element);
        element.setRelation(this);
    }

    @Override
    public boolean add(AttributeDef element) {
        Defense.notNull(element, "element");
        boolean ret = attributes.add(element);
        if (ret) {
            element.setRelation(this);
        }
        return ret;
    }

    @Override
    public Object[] toArray() {
        return attributes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return attributes.toArray(a);
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean remove(Object o) {
        return attributes.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(Collection<? extends AttributeDef> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends AttributeDef> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int lastIndexOf(Object o) {
        return attributes.lastIndexOf(o);
    }

    @Override
    public ListIterator<AttributeDef> listIterator() {
        return attributes.listIterator();
    }

    @Override
    public ListIterator<AttributeDef> listIterator(int index) {
        return attributes.listIterator(index);
    }

    @Override
    public List<AttributeDef> subList(int fromIndex, int toIndex) {
        return attributes.subList(fromIndex, toIndex);
    }

    //Выясним был ли этот внешний ключ описан с родителях
    private static boolean haveForeignKeyInParents(RelationDef relationDef, CrossReference cr) {
        if (relationDef.getOwner() == null) {
            return false;
        } else if (relationDef instanceof ForeignKeyDefImpl
                && ((ForeignKeyDefImpl) relationDef).getCrossReference().fkName.equals(cr.fkName)) {
            return true;
        } else {
            return haveForeignKeyInParents(relationDef.getOwner(), cr);
        }
    }

    @Override
    public RelationType getRelationType() {
        return relation.getRelationType();
    }
}
