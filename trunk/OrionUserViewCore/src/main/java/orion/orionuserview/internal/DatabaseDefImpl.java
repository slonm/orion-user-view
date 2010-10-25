package orion.orionuserview.internal;

import java.io.*;
import java.sql.*;
import java.util.*;
import orion.orionuserview.DatabaseDef;
import orion.orionuserview.EntityDef;
import orion.orionuserview.Relation;
import orion.orionuserview.RelationType;

/**
 *
 * @author sl
 */
//TODO Продумать пробросы исключений
public class DatabaseDefImpl implements Serializable, DatabaseDef {

    private String url;
    private Properties info;
    transient private Connection connection;
    transient private MetadataCache metadataCache = new MetadataCache(this);
//    private Map<Relation, EntityDefImpl> entityDefs = new HashMap<Relation, EntityDefImpl>();
    private boolean storeUser = true;
    private boolean storePassword = false;
    private Set<String> uninterestedAttrPatterns = new HashSet<String>();

    /**
     * For XMLEncoder
     */
    public DatabaseDefImpl() {
    }

    public DatabaseDefImpl(String url, Properties info) {
        this.url = url;
        this.info = info == null ? new Properties() : info;
    }

    public DatabaseDefImpl(String url) {
        this(url, new Properties());
    }

    public DatabaseDefImpl(String url, String username, String password) {
        this(url);
        info.setProperty("user", username);
        info.setProperty("password", password);
    }

    public boolean isStorePassword() {
        return storePassword;
    }

    public void setStorePassword(boolean storePassword) {
        this.storePassword = storePassword;
    }

    public boolean isStoreUser() {
        return storeUser;
    }

    public void setStoreUser(boolean storeUser) {
        this.storeUser = storeUser;
    }

    /**
     * Это множество сущностей - корневых точек для построения дерева
     * @return
     * @throws SQLException
     */
    @Override
    public Set<EntityDef> getEntityDefs() throws SQLException {
        connect();
        //Переформируем множество перед возвратом
        Map<Relation, EntityDefImpl> entityDefs = new HashMap<Relation, EntityDefImpl>();
        for (RelationImpl r : metadataCache.getRelations()) {
            if (r.relationType == RelationType.ENTITY) {
                if (!entityDefs.containsKey(r)) {
                    EntityDefImpl ed = new EntityDefImpl(this, r);
                    entityDefs.put(r, ed);
                }
            } else {
                entityDefs.remove(r);
            }
        }
        return Collections.unmodifiableSet(new HashSet<EntityDef>(entityDefs.values()));
    }

    public Properties getInfo() {
        return info;
    }

    public void setInfo(Properties info) {
        this.info = info;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Соединяется с базой данных
     * Если соединение открыто, то просто возвращает его
     * @return соединение с БД
     */
    @Override
    public Connection connect() throws SQLException {
        validate();
        if (connection == null) {
            connection = DriverManager.getConnection(url, info);
            metadataCache.init();
            forecastRelationType();
        }
        return connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    public Connection connect(String password) throws SQLException {
        info.setProperty("password", password);
        return connect();
    }

    public Connection connect(String username, String password) throws SQLException {
        info.setProperty("user", username);
        return connect(password);
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Отношения для установки флага видимости
     *
     * @return
     * //TODO Что если таблицы уже нет в базе?
     */
    @Override
    public Set<? extends Relation> getRelations() throws SQLException {
        return metadataCache.getRelations();
    }

    private void validate() {
        if (url == null) {
            throw new RuntimeException("URL is null");
        }
    }

    MetadataCache getMetadataCache() {
        return metadataCache;
    }

    /**
     * Шаблоны имен атрибутов, которые не нужно отображать
     * В строках шаблонов первый %s будет заменен на имя таблицы
     */
    @Override
    public Set<String> getHiddenAttrPatterns() {
        return uninterestedAttrPatterns;
    }

    /**
     * for XMLEncoder
     */
    public void setHiddenAttrPatterns(Set<String> uninterestedAttrPatterns) {
        this.uninterestedAttrPatterns = uninterestedAttrPatterns;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private void forecastRelationType() throws SQLException {
        for (Relation relation : getRelations()) {
            Set<CrossReference> crPk = metadataCache.getCrossReferences(relation, null);
            Set<CrossReference> crFk = metadataCache.getCrossReferences(null, relation);
            //Если в таблице и на таблицу нет ссылок, то будем считать ее ненужной
            if (crPk.size() == 0 && crFk.size() == 0) {
                relation.setRelationType(RelationType.HIDDEN);
            } else {
                //Эта таблица каскадно удаляет записи других таблиц
                boolean existsCascadeDeleteForegnKeyInRelation = false;
                for (CrossReference cr : crPk) {
                    if (cr.fkRelation.getRelationType() != RelationType.HIDDEN
                            && cr.cascadeDelete) {
                        existsCascadeDeleteForegnKeyInRelation = true;
                        break;
                    }
                }
                //Другая таблица каскадно удаляет записи этой таблицы
                boolean existsCascadeDeleteForegnKeyInOtherRelation = false;
                for (CrossReference cr : crFk) {
                    if (cr.pkRelation.getRelationType() != RelationType.HIDDEN
                            && cr.cascadeDelete) {
                        existsCascadeDeleteForegnKeyInOtherRelation = true;
                        break;
                    }
                }
                //Если в никакая другая таблица не
                //удаляет каскадно записи этой таблицы, то это или справочник или сущность
                if (!existsCascadeDeleteForegnKeyInOtherRelation) {
                    //Если эта таблица каскадно удаляет записи других таблиц, то это сущность
                    if (existsCascadeDeleteForegnKeyInRelation) {
                        relation.setRelationType(RelationType.ENTITY);
                    } else {
                        relation.setRelationType(RelationType.REFERENCE_BOOK);
                    }
                } else {
                    relation.setRelationType(RelationType.DEPENDENT);
                }
            }
        }
    }

}
