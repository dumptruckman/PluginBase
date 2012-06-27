package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.ResultSet;

public class SQLiteDatabase extends SQLDB {

    @Override
    protected String getName() {
        return "SQLite";
    }

    @Override
    protected DatabaseHandler newDB(PluginBase plugin) {
        return new SQLite(plugin.getPluginName(), plugin.getDataFolder());
    }
}
