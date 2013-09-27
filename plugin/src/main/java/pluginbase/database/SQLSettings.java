package pluginbase.database;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.properties.PropertiesWrapper;

public class SQLSettings extends PropertiesWrapper {

    static {
        SerializationRegistrar.registerClass(SQLSettings.class);
        SerializationRegistrar.registerClass(DatabaseInfo.class);
    }

    @Comment("What type of database to use.  Base options are SQLite and MySQL.")
    private String databaseType = "SQLite";
    private DatabaseInfo databaseInfo = new DatabaseInfo();

    public String getDatabaseType() {
        return databaseType;
    }

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    @Comment("Settings for non-SQLite databases")
    public static class DatabaseInfo {

        private String host = "localhost";
        private String port = "3306";
        private String user = "minecraft";
        private String pass = "";
        private String database = "minecraft";

        private DatabaseInfo() { }

        public String getHost() {
            return host;
        }

        public String getPort() {
            return port;
        }

        public String getUser() {
            return user;
        }

        public String getPass() {
            return pass;
        }

        public String getDatabase() {
            return database;
        }
    }
}
