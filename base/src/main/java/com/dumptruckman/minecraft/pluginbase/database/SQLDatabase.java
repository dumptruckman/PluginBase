package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.sql.ResultSet;

public interface SQLDatabase {

    boolean isConnected();

    boolean connect(PluginBase plugin);

    void disconnect();

    ResultSet query(String string);
}
