package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.sql.ResultSet;

public class MySQLDatabase extends SQLDB {

    @Override
    protected String getName() {
        return "MySQL";
    }

    @Override
    protected DatabaseHandler newDB(PluginBase plugin) {
        return new MySQL(plugin.config().get(BaseConfig.DB_HOST),
                plugin.config().get(BaseConfig.DB_PORT),
                plugin.config().get(BaseConfig.DB_USER),
                plugin.config().get(BaseConfig.DB_PASS),
                plugin.config().get(BaseConfig.DB_DATABASE));
    }
}
