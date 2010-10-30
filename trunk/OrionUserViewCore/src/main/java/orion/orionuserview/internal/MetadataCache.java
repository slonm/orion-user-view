package orion.orionuserview.internal;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import orion.orionuserview.Relation;
import orion.orionuserview.RelationSourceType;
import static orion.orionuserview.utils.SQLUtils.toNullString;
import static orion.orionuserview.utils.SQLUtils.toIdentifier;

/**
 *
 * @author sl
 */
//TODO Проверять открытость соединения
public class MetadataCache {

    private static final String[] TABLE_TYPES = new String[]{"TABLE", "VIEW"};
    private final DatabaseMetaData metadata;
    private Set<RelationImpl> relations = new HashSet<RelationImpl>();
    private boolean isRelationsFilled = false;
    private Map<Relation, List<Column>> columns = new HashMap<Relation, List<Column>>();
    private boolean isColumnsFilled = false;
    private Map<Relation, Set<CrossReference>> pkCrossReference = new HashMap<Relation, Set<CrossReference>>();
    private Map<Relation, Set<CrossReference>> fkCrossReference = new HashMap<Relation, Set<CrossReference>>();
    private boolean isCrossReferencesFilled = false;
    private Map<Relation, Set<UniqueIndex>> tableOnUniqueIndexes = new HashMap<Relation, Set<UniqueIndex>>();
    private boolean isUniqueIndexesFilled = false;

    public MetadataCache(DatabaseMetaData metadata) {
        this.metadata = metadata;
    }

    private synchronized void fillRelations() throws SQLException {
        if (!isRelationsFilled) {
            ResultSet rs = metadata.getTables(null, null, null, TABLE_TYPES);
            while (rs.next()) {
                RelationImpl t = new RelationImpl();
                t.catalog = toNullString(rs, "TABLE_CAT");
                t.schema = toNullString(rs, "TABLE_SCHEM");
                t.name = toNullString(rs, "TABLE_NAME");
                t.remarks = toNullString(rs, "REMARKS");
                t.sourceType = RelationSourceType.valueOf(toNullString(rs, "TABLE_TYPE"));
                //TODO Обновление remarks из базы
                if (!relations.contains(t)) {
                    relations.add(t);
                }

            }
            isRelationsFilled = true;
        }
    }

    Set<RelationImpl> getRelations() throws SQLException {
        return relations;
    }

    /**
     * 
     * @param catalog
     * @param schema
     * @param name
     * @return
     * @throws SQLException
     */
    RelationImpl getRelation(String catalog, String schema, String name) throws SQLException {
        fillRelations();
        for (RelationImpl t : relations) {
            if ((catalog == null || t.catalog.equals(catalog))
                    && (schema == null || t.schema.equals(schema))
                    && t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }

    private synchronized void fillColumns() throws SQLException {
        if (!isColumnsFilled) {
            ResultSet rs = metadata.getColumns(null, null, null, null);
            while (rs.next()) {
                Relation relation = getRelation(toNullString(rs, "TABLE_CAT"),
                        toNullString(rs, "TABLE_SCHEM"),
                        toNullString(rs, "TABLE_NAME"));
                if (relation != null) {
                    Column c = new Column();
                    c.relation = relation;
                    c.name = toNullString(rs, "COLUMN_NAME");
                    c.remarks = toNullString(rs, "REMARKS");
                    c.type = rs.getInt("DATA_TYPE");
                    c.size = rs.getInt("COLUMN_SIZE");
                    c.decimalDigits = rs.getInt("DECIMAL_DIGITS");
                    c.nullable = rs.getBoolean("NULLABLE");
                    c.position = rs.getInt("ORDINAL_POSITION");
                    if (!columns.containsKey(c.relation)) {
                        columns.put(c.relation, new ArrayList<Column>());
                    }
                    columns.get(c.relation).add(c);
                }
            }
            for (List<Column> cs : columns.values()) {
                Collections.sort(cs);
            }
            isColumnsFilled = true;
        }
    }

    List<Column> getColumns(Relation relation)
            throws SQLException {
        fillColumns();
        return columns.get(relation);
    }

    private Collection<CrossReference> fillCrossReferences(ResultSet rs) throws SQLException {
        Map<String, CrossReference> crm = new HashMap<String, CrossReference>();
        while (rs.next()) {
            CrossReference c;
            String key = toNullString(rs, "FK_NAME");
            if (crm.containsKey(key)) {
                c = crm.get(key);
                c.pkOnFk.put(toNullString(rs, "PKCOLUMN_NAME"), toNullString(rs, "FKCOLUMN_NAME"));
            } else {
                Relation pkRelation = getRelation(toNullString(rs, "PKTABLE_CAT"), toNullString(rs, "PKTABLE_SCHEM"), toNullString(rs, "PKTABLE_NAME"));
                Relation fkRelation = getRelation(toNullString(rs, "FKTABLE_CAT"), toNullString(rs, "FKTABLE_SCHEM"), toNullString(rs, "FKTABLE_NAME"));
                if (pkRelation != null && fkRelation != null) {
                    c = new CrossReference();
                    c.pkRelation = pkRelation;
                    c.pkName = toNullString(rs, "PK_NAME");
                    c.fkRelation = fkRelation;
                    c.fkName = toNullString(rs, "FK_NAME");
                    if (rs.getInt("DELETE_RULE") == DatabaseMetaData.importedKeyCascade) {
                        c.cascadeDelete = true;
                    }
                    crm.put(c.fkName, c);
                    c.pkOnFk.put(toNullString(rs, "PKCOLUMN_NAME"), toNullString(rs, "FKCOLUMN_NAME"));
                }
            }
        }
        return crm.values();
    }

    private synchronized void fillCrossReferences() throws SQLException {
        if (!isCrossReferencesFilled) {
            Collection<CrossReference> crm = null;
            //Извлечем все внешние ключи 
            try {
                ResultSet rs = metadata.getCrossReference(null, null, null, null, null, null);
                crm = fillCrossReferences(rs);
            } catch (SQLException ex) {
                //Некоторые JDBC драйвера требуют указания или первичного или 
                //внешнего ключа в getCrossReference
                crm = new HashSet<CrossReference>();
                for (Relation relation : getRelations()) {
                    ResultSet rs = metadata.getCrossReference(
                            toIdentifier(metadata, relation.getCatalog()),
                            toIdentifier(metadata, relation.getSchema()),
                            toIdentifier(metadata, relation.getName()), null, null, null);
                    crm.addAll(fillCrossReferences(rs));
                }
            }
            //сформируем карту с ключем - таблицей первичного ключа
            for (CrossReference c : crm) {
                if (!pkCrossReference.containsKey(c.pkRelation)) {
                    pkCrossReference.put(c.pkRelation, new HashSet());
                }
                pkCrossReference.get(c.pkRelation).add(c);
            }
            //На основе этой карты 
            //сформируем карту с ключем - таблицей внешнего ключа
            for (Set<CrossReference> crs : pkCrossReference.values()) {
                for (CrossReference c : crs) {
                    if (!fkCrossReference.containsKey(c.fkRelation)) {
                        fkCrossReference.put(c.fkRelation, new HashSet());
                    }
                    fkCrossReference.get(c.fkRelation).add(c);
                }
            }
            isCrossReferencesFilled = true;
        }
    }

    Set<CrossReference> getPkCrossReferences(Relation relation)
            throws SQLException {
        fillCrossReferences();
        if (pkCrossReference.containsKey(relation)) {
            return pkCrossReference.get(relation);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    Set<CrossReference> getFkCrossReferences(Relation relation)
            throws SQLException {
        fillCrossReferences();
        if (fkCrossReference.containsKey(relation)) {
            return fkCrossReference.get(relation);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    Set<UniqueIndex> getUniqueIndexes(Relation relation) throws SQLException {
        fillUniqueIndexes();
        if (tableOnUniqueIndexes.containsKey(relation)) {
            return tableOnUniqueIndexes.get(relation);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    private synchronized void fillUniqueIndexes() throws SQLException {
        if (!isUniqueIndexesFilled) {
            Map<String, UniqueIndex> uim = new HashMap<String, UniqueIndex>();
            for (Relation relation : getRelations()) {
                ResultSet rs = metadata.getIndexInfo(
                        toIdentifier(metadata, relation.getCatalog()),
                        toIdentifier(metadata, relation.getSchema()),
                        toIdentifier(metadata, relation.getName()), true, true);
                while (rs.next()) {
                    //Из-за бага в драйвере jayBird дополнительно убедимся что это уникальный индекс
                    if (!rs.getBoolean("NON_UNIQUE")) {
                        UniqueIndex ui;
                        String key = toNullString(rs, "INDEX_NAME");
                        if (uim.containsKey(key)) {
                            ui = uim.get(key);
                            ui.columns.add(toNullString(rs, "COLUMN_NAME"));
                        } else {
                            Relation uiRelation = getRelation(toNullString(rs, "TABLE_CAT"), toNullString(rs, "TABLE_SCHEM"), toNullString(rs, "TABLE_NAME"));
                            if (uiRelation != null) {
                                ui = new UniqueIndex();
                                //таблица может не совпадать с relation
                                ui.relation = uiRelation;
                                ui.name = toNullString(rs, "INDEX_NAME");
                                uim.put(ui.name, ui);
                                //Теперь выясним является ли индекс первичным ключем
                                ResultSet rs1 = metadata.getPrimaryKeys(
                                        toIdentifier(metadata, relation.getCatalog()),
                                        toIdentifier(metadata, relation.getSchema()),
                                        toIdentifier(metadata, relation.getName()));
                                while (rs1.next()) {
                                    if (toNullString(rs1, "PK_NAME").equals(ui.name)) {
                                        ui.isPrimaryKey = true;
                                        break;
                                    }
                                }
                                rs1.close();
                                ui.columns.add(toNullString(rs, "COLUMN_NAME"));
                            }
                        }
                    }
                }
            }
            for (UniqueIndex ui : uim.values()) {
                if (!tableOnUniqueIndexes.containsKey(ui.relation)) {
                    tableOnUniqueIndexes.put(ui.relation, new HashSet());
                }
                tableOnUniqueIndexes.get(ui.relation).add(ui);
            }
            isUniqueIndexesFilled = true;
        }
    }

    void init() throws SQLException {
        fillRelations();
        fillColumns();
        fillCrossReferences();
        fillUniqueIndexes();
    }
}
