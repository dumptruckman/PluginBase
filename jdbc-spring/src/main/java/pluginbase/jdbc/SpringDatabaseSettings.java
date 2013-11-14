package pluginbase.jdbc;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.properties.PropertiesWrapper;

public class SpringDatabaseSettings extends PropertiesWrapper implements DatabaseSettings {

    static {
        SerializationRegistrar.registerClass(SpringDatabaseSettings.class);
        SerializationRegistrar.registerClass(SpringDatabaseConnectionInfo.class);
    }

    @Comment({
            "What type of database to use.",
            "H2 is built in.",
            "Others such as MySQL or SQLite may work.",
            "You can also specify the exact driver to use here."
    })
    private String databaseType;
    private SpringDatabaseConnectionInfo databaseInfo = new SpringDatabaseConnectionInfo();

    public SpringDatabaseSettings() {
        this("H2");
    }

    public SpringDatabaseSettings(String defaultDbType) {
        this.databaseType = defaultDbType;
    }

    @Override
    public String getDatabaseType() {
        return databaseType;
    }

    @Override
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public SpringDatabaseConnectionInfo getDatabaseInfo() {
        return databaseInfo;
    }

    @Comment("Settings for non-SQLite databases")
    public static class SpringDatabaseConnectionInfo implements DatabaseConnectionInfo {

        @Comment({
                "This is the JDBC url that will be used for connecting to the database.",
                "If this does not start with \"jdbc\" then it will be assumed an embedded db is desired and this will be the file name for such."
        })
        private String url = "database";
        private String user = "sa";
        private String pass = "";

        private SpringDatabaseConnectionInfo() { }

        @Override
        public String getUser() {
            return user;
        }

        @Override
        public String getPass() {
            return pass;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void setUser(String user) {
            this.user = user;
        }

        @Override
        public void setPass(String pass) {
            this.pass = pass;
        }
    }
}
