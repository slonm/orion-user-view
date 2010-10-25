/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.internal;

import java.sql.SQLException;
import java.util.*;
import orion.orionuserview.DatabaseDef;
import orion.orionuserview.EntityDef;
import orion.orionuserview.Relation;
import orion.orionuserview.utils.Defense;

/**
 *
 * @author sl
 */
public class EntityDefImpl extends RelationDefImpl implements EntityDef {

    private Set<Relation> additionalReferenceBooks = new HashSet<Relation>();
    private DatabaseDefImpl databaseDef;
    EntityDefImpl(DatabaseDefImpl databaseDef, Relation relation) throws SQLException {
        super(relation, null, null);
        setRootEntityDefImpl(this);
        this.databaseDef=Defense.notNull(databaseDef, "databaseDef");
    }

    @Override
    public DatabaseDef getDatabaseDef() {
        return databaseDef;
    }

    public DatabaseDefImpl getDatabaseDefImpl() {
        return databaseDef;
    }

    @Override
    public Set<Relation> getAdditionalReferenceBooks() {
        return additionalReferenceBooks;
    }

    public void setAdditionalReferenceBooks(Set<Relation> additionalReferenceBooks) {
        this.additionalReferenceBooks = additionalReferenceBooks;
    }

}
