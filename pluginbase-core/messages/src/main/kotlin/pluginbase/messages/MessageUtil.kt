/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
@file:JvmName("MessageUtil")
package pluginbase.messages

import java.util.IllegalFormatException
import java.util.Locale

/**
 * Formats a string by replacing ampersand with the Section symbol and %s with the corresponding args
 * object in a fashion similar to String.format().

 * @param message Message to format.
 * @param args    Arguments to pass in via %n.
 * @return The formatted message.
 */
@Throws(IllegalFormatException::class)
fun formatMessage(locale: Locale, message: String, vararg args: Any): String {
    var result = message
    result = ChatColor.translateAlternateColorCodes('&', result)
    // TODO need a fix for this when language vars are not passed in as args.
    result = String.format(locale, result, *args)
    return result
}