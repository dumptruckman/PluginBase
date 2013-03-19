/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import com.dumptruckman.minecraft.pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A marker interface used to indicate that something will be using localized messages.
 * <p/>
 * This is required to register messages for localization.  See {@link Messages#registerMessages(Localizable, Class)}.
 */
public interface Localizable {

    /**
     * Localizable requires a method to acquire a logger due to the needs of the default implementations of the
     * classes that utilize the Localizable interface to log messages.
     *
     * @return a PluginLogger for this Localizable.
     */
    @NotNull
    PluginLogger getLog();
}
