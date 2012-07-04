package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.ResultSet;

public abstract class SQLDB implements SQLDatabase {

    protected DatabaseHandler db = null;

    private boolean connected = false;

    @Override
    public ResultSet query(String query) {
        try {
            ResultSet result = db.query(query);
            Logging.finest("SQL: " + query);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    protected abstract String getName();

    protected abstract DatabaseHandler newDB(PluginBase plugin);

    @Override
    public boolean connect(PluginBase plugin) {
        if (isConnected()) {
            return false;
        }
        if (db == null) {
            db = newDB(plugin);
        }
        try {
            db.open();
            if (!db.checkConnection()) {
                return false;
            }
        } catch (Exception e) {
            Logging.severe("Connection to " + getName() + " database failed.");
            return false;
        }
        Logging.finest("Connected to " + getName() + " database.");
        connected = true;
        return true;
    }

    @Override
    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        db.close();
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean createTable(String query) {
        return db.createTable(query);
    }

    @Override
    public boolean checkTable(String name) {
        return db.checkTable(name);
    }

    @Override
    public boolean wipeTable(String table) {
        return db.wipeTable(table);
    }
}
