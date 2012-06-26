package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.ResultSet;

public class MySQLDatabase implements SQLDatabase {

    private MySQL db;
    private boolean connected = false;

    @Override
    public ResultSet query(String query) {
        try {
            return db.query(query);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean connect(PluginBase plugin) {
        if (isConnected()) {
            return false;
        }
        Logging.fine("Connecting to MySQL database.");
        db = new MySQL(plugin.config().get(BaseConfig.DB_HOST),
                plugin.config().get(BaseConfig.DB_PORT),
                plugin.config().get(BaseConfig.DB_USER),
                plugin.config().get(BaseConfig.DB_PASS),
                plugin.config().get(BaseConfig.DB_DATABASE));

        try {
            db.open();
            if (!db.checkConnection()) {
                return false;
            }
        } catch (Exception e) {
            Logging.severe("Connection to MySQL database failed.");
            return false;
        }
        Logging.fine("Connected to MySQL database.");
        connected = true;
        return true;
    }

    @Override
    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        db.close();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}
