package pluginbase.plugin;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.properties.PropertiesWrapper;

public class SQLSettings extends PropertiesWrapper {

    static {
        SerializationRegistrar.registerClass(SQLSettings.class);
        SerializationRegistrar.registerClass(DatabaseInfo.class);
    }

    @Comment({
            "What type of database to use.",
            "H2 is built in.",
            "Others such as MySQL or SQLite may work.",
            "You can also specify the exact driver to use here."
    })
    private String databaseType = "H2";
    private DatabaseInfo databaseInfo = new DatabaseInfo();

    public SQLSettings() { }

    public String getDatabaseType() {
        return databaseType;
    }

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    @Comment("Settings for non-SQLite databases")
    public static class DatabaseInfo {

        @Comment({
                "This is the JDBC url that will be used for connecting to the database.",
                "If this does not start with \"jdbc\" then it will be assumed an embedded db is desired and this will be the file name for such."
        })
        private String url = "database";
        private String user = "sa";
        private String pass = "";

        private DatabaseInfo() { }

        public String getUser() {
            return user;
        }

        public String getPass() {
            return pass;
        }

        public String getUrl() {
            return url;
        }
    }
}
