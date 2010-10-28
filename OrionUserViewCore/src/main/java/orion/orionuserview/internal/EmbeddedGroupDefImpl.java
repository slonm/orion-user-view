package orion.orionuserview.internal;

import java.sql.SQLException;
import orion.orionuserview.RelationSourceType;

/**
 * Группа полей таблицы.
 * По аналогии с Embedded в Hibernate
 * @author sl
 */

//TODO Чем группа отличается от внешнего ключа OneToOne?
public class EmbeddedGroupDefImpl extends RelationDefImpl{

//    public EmbeddedGroupDefImpl(MetadataCache databaseInfo, String catalog,
//            String schema, String name,
//            RelationDefImpl relation, RelationSourceType type) {
//        super(databaseInfo, catalog, schema, name, relation, type, null);
//    }

    @Override
    public RelationSourceType getRelationSourceType() {
        return RelationSourceType.GROUP;
    }

    @Override
    public void init() throws SQLException {
        throw new RuntimeException();
    }

    @Override
    public String getAlias() {
        return getOwner().getAlias();
    }

}
