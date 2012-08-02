/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.database;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

public class MySQLDatabase extends SQLDB {

    @Override
    protected String getName() {
        return "MySQL";
    }

    @Override
    protected DatabaseHandler newDB(PluginBase plugin) {
        return new MySQL(plugin.config().get(BaseConfig.DB_HOST),
                plugin.config().get(BaseConfig.DB_PORT),
                plugin.config().get(BaseConfig.DB_DATABASE),
                plugin.config().get(BaseConfig.DB_USER),
                plugin.config().get(BaseConfig.DB_PASS));
    }
}
