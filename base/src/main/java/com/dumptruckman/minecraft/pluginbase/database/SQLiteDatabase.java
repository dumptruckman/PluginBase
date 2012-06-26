package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.ResultSet;

public class SQLiteDatabase implements SQLDatabase {

    private SQLite db;
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
        Logging.fine("Connecting to SQLite database.");
        db = new SQLite(plugin.getPluginName(), plugin.getDataFolder());

        try {
            db.open();
            if (!db.checkConnection()) return false;
        } catch (Exception e) {
            Logging.severe("Connection to SQLite database failed.");
            return false;
        }
        Logging.fine("Connected to SQLite database.");
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
