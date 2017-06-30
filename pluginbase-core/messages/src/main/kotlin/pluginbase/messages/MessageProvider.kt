/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import java.util.Locale

/**
 * Indicates that localized messages can be retrieved from this class.
 *
 * See [Messages.loadMessages] for a simple flat file implementation.
 */
interface MessageProvider {

    /**
     * Returns a message (as [String]) for the specified key (as [pluginbase.messages.Messages]).
     *
     * This is a localized message.
     *
     * @param key  the message key.
     * @param args arguments for String.format().
     * @return the localized message.
     */
    fun getLocalizedMessage(key: Message, vararg args: Any?): String

    /**
     * Returns a message (as [String]) for the specified key (as [pluginbase.messages.Messages]).
     *
     * This is a localized message.
     *
     * @param key  the message key.
     * @param args arguments for String.format().
     * @return the localized message.
     */
    fun getLocalizedMessage(key: Array<Any>, vararg args: Any?): String

    /**
     * Returns the plugin this message provider provides messages for.
     *
     * @return the plugin this message provider provides messages for.
     */
    val plugin: LocalizablePlugin

    /**
     * The default locale to use.
     **/
    companion object DEFAULT_LOCALE {

        val value = Locale.US
    }
}

