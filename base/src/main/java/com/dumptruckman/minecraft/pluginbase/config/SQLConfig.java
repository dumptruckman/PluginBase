/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface SQLConfig {

    SimpleProperty<String> DB_TYPE = PropertyFactory.newProperty(String.class, "database_type", "SQLite")
            .comment("# What type of database to use.  Base options are SQLite and MySQL.").build();

    NullProperty DB_SETTINGS = PropertyFactory.newNullProperty("settings")
            .comment("# Settings for non-SQLite databases").build();

    SimpleProperty<String> DB_HOST = PropertyFactory.newProperty(String.class, "settings.host", "localhost").build();
    SimpleProperty<String> DB_PORT = PropertyFactory.newProperty(String.class, "settings.port", "3306").build();
    SimpleProperty<String> DB_USER = PropertyFactory.newProperty(String.class, "settings.user", "minecraft").build();
    SimpleProperty<String> DB_PASS = PropertyFactory.newProperty(String.class, "settings.pass", "").build();
    SimpleProperty<String> DB_DATABASE = PropertyFactory.newProperty(String.class, "settings.database", "minecraft").build();
}
