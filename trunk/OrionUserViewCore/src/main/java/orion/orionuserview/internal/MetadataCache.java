/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.internal;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import orion.orionuserview.DatabaseDef;
import orion.orionuserview.Relation;
import orion.orionuserview.RelationSourceType;
import static orion.orionuserview.utils.SQLUtils.toNullString;
import static orion.orionuserview.utils.SQLUtils.toIdentifier;

/**
 *
 * @author sl
 */
//TODO Проверять открытость соединения
//TODO Может убрать все клонирование?
public class MetadataCache {

    private static final String[] TABLE_TYPES = new String[]{"TABLE", "VIEW"};
    private final DatabaseDef databaseDef;
    Set<RelationImpl> relations = new HashSet<RelationImpl>();
    private boolean isRelationsFilled = false;
    private Map<Relation, List<Column>> columns = new HashMap<Relation, List<Column>>();
    private boolean isColumnsFilled = false;
    private Set<CrossReference> crossReferences = new HashSet<CrossReference>();
    private boolean isCrossReferencesFilled = false;
    private Map<Relation, Set<UniqueIndex>> tableOnUniqueIndexes = new HashMap<Relation, Set<UniqueIndex>>();

    public MetadataCache(DatabaseDef databaseDef) {
        this.databaseDef = databaseDef;
    }

    private void fillRelations() throws SQLException {
        if (!isRelationsFilled && databaseDef.getConnection() != null) {
            ResultSet rs = databaseDef.getConnection().getMetaData().getTables(null, null, null, TABLE_TYPES);
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

    private void fillColumns() throws SQLException {
        if (!isColumnsFilled && databaseDef.getConnection() != null) {
            ResultSet rs = databaseDef.getConnection().getMetaData().getColumns(null, null, null, null);
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

    private void fillCrossReferences() throws SQLException {
        if (!isCrossReferencesFilled && databaseDef.getConnection() != null) {
            ResultSet rs = databaseDef.getConnection().getMetaData().getCrossReference(null, null, null, null, null, null);
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
            crossReferences.addAll(crm.values());
            isCrossReferencesFilled = true;
        }
    }

    Set<CrossReference> getCrossReferences(Relation pkRelation, Relation fkRelation)
            throws SQLException {
        fillCrossReferences();
        Set<CrossReference> ret = new HashSet<CrossReference>();
        for (CrossReference cr : crossReferences) {

            if ((pkRelation == null || cr.pkRelation.equals(pkRelation))
                    && (fkRelation == null || cr.fkRelation.equals(fkRelation))) {
                ret.add(cr);
            }
        }
        return ret;
    }

    Set<UniqueIndex> getUniqueIndexes(Relation relation) throws SQLException {
        if (!tableOnUniqueIndexes.containsKey(relation) && databaseDef.getConnection() != null) {
            ResultSet rs = databaseDef.getConnection().getMetaData().getIndexInfo(
                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getCatalog()),
                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getSchema()),
                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getName()), true, true);
            Map<String, UniqueIndex> uim = new HashMap<String, UniqueIndex>();
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
                            ResultSet rs1 = databaseDef.getConnection().getMetaData().getPrimaryKeys(
                                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getCatalog()),
                                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getSchema()),
                                    toIdentifier(databaseDef.getConnection().getMetaData(), relation.getName()));
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
            for (UniqueIndex ui : uim.values()) {
                if (!tableOnUniqueIndexes.containsKey(ui.relation)) {
                    tableOnUniqueIndexes.put(relation, new HashSet());
                }
                tableOnUniqueIndexes.get(ui.relation).add(ui);
            }
        }
        return tableOnUniqueIndexes.get(relation);
    }

    void init() throws SQLException {
        fillRelations();
        fillColumns();
        fillCrossReferences();
    }
}
