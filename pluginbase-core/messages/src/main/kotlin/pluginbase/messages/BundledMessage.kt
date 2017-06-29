/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

/**
 * Represents a [Message] preset with arguments to fill in any instances of `%s`.
 *
 * Can be used in cases where you are required to return a message of some sort and it is otherwise impossible to
 * return a localized message due to also requiring arguments.
 *
 * See [Message.bundle] for creation of these bundled messages.
 */
class BundledMessage internal constructor(
        /**
         * Gets the localization message for this bundle.
         *
         * @return the localization message for this bundle.
         */
        val message: Message,
        /**
         * Gets the arguments for the bundled message.
         *
         * @return the arguments for the bundled message.
         */
        val args: Array<out Any>)
