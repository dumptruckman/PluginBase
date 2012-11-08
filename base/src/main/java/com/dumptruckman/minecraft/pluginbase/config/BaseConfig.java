/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.MessageProvider;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertySerializer;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

import java.util.Locale;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface BaseConfig extends Properties {

    /**
     * Locale name config path, default and comments.
     */ //TODO Add more comments about acceptable locales.
    SimpleProperty<Locale> LOCALE = PropertyFactory.newProperty(Locale.class, "settings.language.locale",
            Locale.ENGLISH)
            .comment("# This is the locale you wish to use.").serializer(new PropertySerializer<Locale>() {
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
    SimpleProperty<String> LANGUAGE_FILE = PropertyFactory.newProperty(String.class, "settings.language.file",
            MessageProvider.DEFAULT_LANGUAGE_FILE_NAME).comment("# This is the language file you wish to use.")
            .build();

    /**
     * Debug Mode config path, default and comments.
     */
    SimpleProperty<Integer> DEBUG_MODE = PropertyFactory.newProperty(Integer.class, "settings.debug_level", 0)
            .comment("# 0 = off, 1-3 display debug info with increasing granularity.").validator(new PropertyValidator() {
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
    SimpleProperty<Boolean> FIRST_RUN = PropertyFactory.newProperty(Boolean.class, "settings.first_run", true)
            .comment("# Will make the plugin perform tasks only done on a first run (if any.)").build();
}
