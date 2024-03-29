package orion.orionuserview;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Рекомендуемое использование:
 * 1) вызвать одну из версий connect()
 * 2) выполить функцию forecastRelationTypeOne()
 * 3) вручную скорректировать скрытые таблицы
 * 4) выполнить функцию forecastRelationTypeTwo()
 * 5) вручную скорректировать сущности и справочники
 * 6) выполнить функцию forecastRelationTypeThree()
 * 7) вручную скорректировать группы, атрибуты и таблицы пересечений
 * @author sl
 */
public interface DatabaseDef {

    Set<? extends Relation> getRelations() throws SQLException;

    Set<String> getHiddenAttrPatterns();

    Connection getConnection();

    Connection connect() throws SQLException;

    boolean isConnected();

    Set<EntityDef> getEntityDefs() throws SQLException;

    public String newAlias(RelationDef relation);
}
