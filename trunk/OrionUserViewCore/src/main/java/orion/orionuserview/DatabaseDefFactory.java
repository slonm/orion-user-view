package orion.orionuserview;

import java.util.Properties;
import orion.orionuserview.internal.DatabaseDefImpl;

/**
 *
 * @author sl
 */
public class DatabaseDefFactory {

    private DatabaseDefFactory() {
    }

    public static DatabaseDef build(String url) {
        return new DatabaseDefImpl(url);
    }

    public static DatabaseDef build(String url, Properties info) {
        return new DatabaseDefImpl(url, info);
    }

    public static DatabaseDef build(String url, String username, String password) {
        return new DatabaseDefImpl(url, username, password);
    }
}
