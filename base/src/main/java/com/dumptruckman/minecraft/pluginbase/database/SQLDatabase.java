/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.sql.ResultSet;

public interface SQLDatabase {

    boolean isConnected();

    boolean connect(PluginBase plugin);

    void disconnect();

    ResultSet query(String string);

    boolean createTable(String query);

    boolean checkTable(String name);

    boolean wipeTable(String table);
}
