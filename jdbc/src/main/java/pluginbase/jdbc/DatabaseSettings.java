package pluginbase.jdbc;

import pluginbase.config.properties.Properties;

public interface DatabaseSettings extends Properties {

    String getDatabaseType();

    void setDatabaseType(String dbType);

    DatabaseConnectionInfo getDatabaseInfo();

    interface DatabaseConnectionInfo {

        String getUser();

        void setUser(String user);

        String getPass();

        void setPass(String pass);

        String getUrl();

        void setUrl(String url);
    }
}
