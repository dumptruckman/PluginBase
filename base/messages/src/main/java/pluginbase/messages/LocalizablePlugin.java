/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import pluginbase.logging.LogProvider;
import pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A marker interface used to indicate a plugin that will be using localizable messages.
 * <p/>
 * This is required to register messages for localization.  See {@link Messages#registerMessages(LocalizablePlugin, Class)}.
 */
public interface LocalizablePlugin extends LogProvider {

}
