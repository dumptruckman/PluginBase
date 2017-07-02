/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import pluginbase.logging.Logging

interface Message {
    /**
     * The default message in whatever your plugin's primary language is.
     *
     * @return The default non-localized messages.
     */
    val default: String
    /**
     * The localization key for the message. Each object in the array represents a configuration node path.
     *
     * @return The localization key for the message.
     */
    val key: Array<*>?
    /**
     * Gets the number of expected arguments for this message.
     *
     * This is used to validate localized versions of this message to ensure they were given the appropriate
     * amount of arguments.
     *
     * @return the number of expected arguments for this message.
     */
    val argCount: Int

    /**
     * Bundles this message with preset arguments.
     *
     * Can be used in cases where you are required to return a message of some sort and it is otherwise impossible to
     * return a localized message due to also requiring arguments.
     *
     * Should the number of [args] not match the expected arg count for this [Message], a warning will be logged to
     * help determine where this occurred.
     *
     * @param args The arguments for the bundled message.
     */
    fun bundle(vararg args: Any?): BundledMessage {
        if (args.size != argCount) {
            Logging.warning("Bundled message created without appropriate number of arguments!")
            for (e in Thread.currentThread().stackTrace) {
                Logging.warning(e.toString())
            }
        }
        return BundledMessage(this, args)
    }
}
