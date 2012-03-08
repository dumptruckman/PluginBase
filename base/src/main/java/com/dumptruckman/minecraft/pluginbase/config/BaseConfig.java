package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.MessageProvider;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;

import java.util.List;
import java.util.Locale;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface BaseConfig extends IConfig {

    /**
     * Locale name config path, default and comments.
     */ //TODO Add more comments about acceptable locales.
    ConfigEntry<Locale> LOCALE = new AdvancedConfigEntry<Locale>(Locale.class, "settings.language.locale",
            Locale.ENGLISH, "# This is the locale you wish to use.") {

        @Override
        public Object serialize(Locale value) {
            return value.toString();
        }

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
    };

    /**
     * Locale name config path, default and comments.
     */
    ConfigEntry<String> LANGUAGE_FILE = new SimpleConfigEntry<String>(String.class, "settings.language.file",
            MessageProvider.DEFAULT_LANGUAGE_FILE_NAME, "# This is the language file you wish to use.");

    /**
     * Debug Mode config path, default and comments.
     */
    ConfigEntry<Integer> DEBUG_MODE = new SimpleConfigEntry<Integer>(Integer.class, "settings.debug_level", 0,
            "# 0 = off, 1-3 display debug info with increasing granularity.") {
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
    };

    /**
     * First Run flag config path, default and comments.
     */
    ConfigEntry<Boolean> FIRST_RUN = new SimpleConfigEntry<Boolean>(Boolean.class, "settings.first_run", true,
            "# Will make the plugin perform tasks only done on a first run (if any.)");
}
