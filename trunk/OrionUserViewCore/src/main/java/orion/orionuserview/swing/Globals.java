/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview.swing;

import orion.orionuserview.DatabaseDef;

/**
 *
 * @author sl
 */
public interface Globals {

    DatabaseDef getDatabaseDef();

    void setDatabaseDef(DatabaseDef databaseDef);

    void addHint(String message);

    void addInfo(String message);

    void addWarning(String message);

    void addError(String message);

    <T> T getParametr(Class<T> clasz, String key);

}
