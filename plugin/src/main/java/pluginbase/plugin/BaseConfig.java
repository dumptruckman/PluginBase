/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin;

import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.plugin.command.builtin.DebugCommand;
import pluginbase.properties.PropertyFactory;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.SimpleProperty;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Contains all the basic config options available in a PluginBase plugin.
 */
public interface BaseConfig {

    /**
     * Locale name config key, default and comments.
     */ //TODO Add more comments about acceptable locales.
    SimpleProperty<Locale> LOCALE = PropertyFactory.newProperty(Locale.class, "settings.language.locale",
            MessageProvider.DEFAULT_LOCALE)
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
     * Locale name config key, default and comments.
     */
    SimpleProperty<String> LANGUAGE_FILE = PropertyFactory.newProperty(String.class, "settings.language.file",
            MessageProvider.DEFAULT_LANGUAGE_FILE_NAME).comment("# This is the language file you wish to use.")
            .build();

    /**
     * Debug Mode config key, default and comments.
     */
    SimpleProperty<Integer> DEBUG_MODE = PropertyFactory.newProperty(Integer.class, "settings.debug_level", 0)
            .comment("# 0 = off, 1-3 display debug info with increasing granularity.").validator(new PropertyValidator<Integer>() {
                @Override
                public boolean isValid(@Nullable final Integer obj) {
                    return obj != null && obj >= 0 && obj <= 3;
                }

                @NotNull
                @Override
                public Message getInvalidMessage() {
                    return DebugCommand.INVALID_DEBUG;
                }
            }).build();

    /**
     * First Run flag config key, default and comments.
     */
    SimpleProperty<Boolean> FIRST_RUN = PropertyFactory.newProperty(Boolean.class, "settings.first_run", true)
            .comment("# Will make the plugin perform tasks only done on a first run (if any.)").build();
}
