package com.dumptruckman.tools.config;

import com.dumptruckman.tools.locale.MessageProvider;

import java.util.List;
import java.util.Locale;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface BaseConfig {

    /**
     * Locale name config path, default and comments.
     */ //TODO Add more comments about acceptable locales.
    static final ConfigEntry<Locale> LOCALE = new AdvancedConfigEntry<Locale, String>("settings.language.locale", "en",
            "# This is the locale you wish to use.") {
        @Override
        public String convertForSet(Locale locale) {
            return locale.toString();
        }

        @Override
        public Locale convertForGet(String s) {
            String[] split = s.split("_");
            switch (split.length) {
                case 1:
                    return new Locale(split[0]);
                case 2:
                    return new Locale(split[0], split[1]);
                case 3:
                    return new Locale(split[0], split[1], split[2]);
                default:
                    return new Locale(s);
            }
        }
    };

    /**
     * Locale name config path, default and comments.
     */
    static final ConfigEntry<String> LANGUAGE_FILE = new SimpleConfigEntry<String>("settings.language.file",
            MessageProvider.DEFAULT_LANGUAGE_FILE_NAME, "# This is the language file you wish to use.");

    /**
     * Debug Mode config path, default and comments.
     */
    static final ConfigEntry<Integer> DEBUG_MODE = new SimpleConfigEntry<Integer>("settings.debug_level", 0,
            "# 0 = off, 1-3 display debug info with increasing granularity.");

    /**
     * First Run flag config path, default and comments.
     */
    static final ConfigEntry<Boolean> FIRST_RUN = new SimpleConfigEntry<Boolean>("settings.first_run", true,
            "# Will make the plugin perform tasks only done on a first run (if any.)");
    
    /**
     * Convenience method for saving the config to disk.
     */
    void save();

    Locale get(ConfigEntry<Locale> entry);

    void set(ConfigEntry entry, Object newValue);
    
    Integer get(ConfigEntry<Integer> entry);
    Boolean get(ConfigEntry<Boolean> entry);
    String get(ConfigEntry<String> entry);
    //List get(ConfigEntry<List> entry);
    List get(ConfigEntry<List<String>> entry);

    
    //Object get(ConfigEntry entry);
    
   // void set(ConfigEntry entry, Object newValue);
}
