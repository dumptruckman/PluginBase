package pluginbase.jdbc;

import pluginbase.config.properties.Properties;

public interface DatabaseSettings extends Properties {

    String getDatabaseType();

    DatabaseConnectionInfo getDatabaseInfo();

    interface DatabaseConnectionInfo {

        String getUser();

        String getPass();

        String getUrl();
    }
}
