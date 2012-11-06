/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.util.Null;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface SQLConfig extends Properties {

    SimpleProperty<String> DB_TYPE = new PropertyBuilder<String>(String.class, "database_type").def("SQLite")
            .comment("# What type of database to use.  Base options are SQLite and MySQL.").build();

    SimpleProperty<Null> DB_SETTINGS = new PropertyBuilder<Null>(Null.class, "settings")
            .comment("# Settings for non-SQLite databases").build();

    SimpleProperty<String> DB_HOST = new PropertyBuilder<String>(String.class, "settings.host").def("localhost").build();
    SimpleProperty<String> DB_PORT = new PropertyBuilder<String>(String.class, "settings.port").def("3306").build();
    SimpleProperty<String> DB_USER = new PropertyBuilder<String>(String.class, "settings.user").def("minecraft").build();
    SimpleProperty<String> DB_PASS = new PropertyBuilder<String>(String.class, "settings.pass").def("").build();
    SimpleProperty<String> DB_DATABASE = new PropertyBuilder<String>(String.class, "settings.database").def("minecraft").build();
}
