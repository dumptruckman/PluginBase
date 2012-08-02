/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.MessageProvider;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;
import com.dumptruckman.minecraft.pluginbase.util.Null;

import java.util.Locale;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface BaseConfig extends Config {

    /**
     * Locale name config path, default and comments.
     */ //TODO Add more comments about acceptable locales.
    SimpleConfigEntry<Locale> LOCALE = new EntryBuilder<Locale>(Locale.class, "settings.language.locale").def(Locale.ENGLISH)
            .comment("# This is the locale you wish to use.").serializer(new EntrySerializer<Locale>() {
                @Override
                public Locale deserialize(Object o) {
                    String[] split = o.toString().split("_");
                    switch (split.length) {
                        case 1:
                            return new Locale(split[0]);
                        case 2:
                            return new Locale(split[0], split[1]);
                        case 3:
                            return new Locale(split[0], split[1], split[2]);
                        default:
                            return new Locale(o.toString());
                    }
                }

                @Override
                public Object serialize(Locale locale) {
                    return locale.toString();
                }
            }).build();

    /**
     * Locale name config path, default and comments.
     */
    SimpleConfigEntry<String> LANGUAGE_FILE = new EntryBuilder<String>(String.class, "settings.language.file")
            .def(MessageProvider.DEFAULT_LANGUAGE_FILE_NAME).comment("# This is the language file you wish to use.")
            .build();

    /**
     * Debug Mode config path, default and comments.
     */
    SimpleConfigEntry<Integer> DEBUG_MODE = new EntryBuilder<Integer>(Integer.class, "settings.debug_level").def(0)
            .comment("# 0 = off, 1-3 display debug info with increasing granularity.").validator(new EntryValidator() {
                @Override
                public boolean isValid(Object obj) {
                    try {
                        int value = Integer.parseInt(obj.toString());
                        if (value >= 0 && value <= 3) {
                            return true;
                        }
                    } catch (NumberFormatException ignore) { }
                    return false;
                }

                @Override
                public Message getInvalidMessage() {
                    return Messages.INVALID_DEBUG;
                }
            }).build();

    /**
     * First Run flag config path, default and comments.
     */
    SimpleConfigEntry<Boolean> FIRST_RUN = new EntryBuilder<Boolean>(Boolean.class, "settings.first_run").def(true)
            .comment("# Will make the plugin perform tasks only done on a first run (if any.)").build();

    SimpleConfigEntry<String> DB_TYPE = new EntryBuilder<String>(String.class, "database.database_type").def("SQLite")
            .comment("# What type of database to use, if the plugin uses a database.  Base options are SQLite and MySQL.").build();

    SimpleConfigEntry<Null> DB_SETTINGS = new EntryBuilder<Null>(Null.class, "database.settings")
            .comment("# Settings for non-SQLite databases").build();

    SimpleConfigEntry<String> DB_HOST = new EntryBuilder<String>(String.class, "database.settings.host").def("localhost").build();
    SimpleConfigEntry<String> DB_PORT = new EntryBuilder<String>(String.class, "database.settings.port").def("3306").build();
    SimpleConfigEntry<String> DB_USER = new EntryBuilder<String>(String.class, "database.settings.user").def("minecraft").build();
    SimpleConfigEntry<String> DB_PASS = new EntryBuilder<String>(String.class, "database.settings.pass").def("").build();
    SimpleConfigEntry<String> DB_DATABASE = new EntryBuilder<String>(String.class, "database.settings.database").def("minecraft").build();
}
