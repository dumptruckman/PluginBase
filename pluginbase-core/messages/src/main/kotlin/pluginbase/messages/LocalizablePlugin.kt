/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import pluginbase.logging.LogProvider

/**
 * A marker interface used to indicate a plugin that will be using localizable messages.
 *
 * This is required to register messages for localization.  See [Messages.registerMessages].
 */
interface LocalizablePlugin : LogProvider
