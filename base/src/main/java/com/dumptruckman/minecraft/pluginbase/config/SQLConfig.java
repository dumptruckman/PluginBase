/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.util.Null;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface SQLConfig extends Config {

    SimpleConfigEntry<String> DB_TYPE = new EntryBuilder<String>(String.class, "database_type").def("SQLite")
            .comment("# What type of database to use.  Base options are SQLite and MySQL.").build();

    SimpleConfigEntry<Null> DB_SETTINGS = new EntryBuilder<Null>(Null.class, "settings")
            .comment("# Settings for non-SQLite databases").build();

    SimpleConfigEntry<String> DB_HOST = new EntryBuilder<String>(String.class, "settings.host").def("localhost").build();
    SimpleConfigEntry<String> DB_PORT = new EntryBuilder<String>(String.class, "settings.port").def("3306").build();
    SimpleConfigEntry<String> DB_USER = new EntryBuilder<String>(String.class, "settings.user").def("minecraft").build();
    SimpleConfigEntry<String> DB_PASS = new EntryBuilder<String>(String.class, "settings.pass").def("").build();
    SimpleConfigEntry<String> DB_DATABASE = new EntryBuilder<String>(String.class, "settings.database").def("minecraft").build();
}
