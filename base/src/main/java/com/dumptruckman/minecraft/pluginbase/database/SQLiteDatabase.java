/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

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
