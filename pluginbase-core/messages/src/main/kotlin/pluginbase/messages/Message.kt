/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import org.xml.sax.SAXException
import pluginbase.logging.Logging
import java.io.IOException
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * A localization key and its defaults.
 *
 * The key represents the location of the localized strings in a language file.
 *
 * The default is what should populate the localization file by default.
 */
class Message private constructor (key: String?, default: String) {

    /**
     * The default message in whatever your plugin's primary language is.
     *
     * @return The default non-localized messages.
     */
    val default: String = Theme.parseMessage(default)

    /**
     * The localization key for the message. Each object in the array represents a configuration node path.
     *
     * @return The localization key for the message.
     */
    val key: Array<Any>? = key?.split(".")?.dropLastWhile { it.isEmpty() }?.toTypedArray()

    /**
     * Gets the number of expected arguments for this message.
     *
     * This is used to validate localized versions of this message to ensure they were given the appropriate
     * amount of arguments.
     *
     * @return the number of expected arguments for this message.
     */
    val argCount: Int = countArgs(this.default)

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
    fun bundle(vararg args: Any): BundledMessage {
        if (args.size != argCount) {
            Logging.warning("Bundled message created without appropriate number of arguments!")
            for (e in Thread.currentThread().stackTrace) {
                Logging.warning(e.toString())
            }
        }
        return BundledMessage(this, args)
    }

    override fun toString(): String {
        return "Message{def='$default', key='$key', argCount='$argCount'}"
    }

    companion object {

        /**
         * Creates a new localized message.
         *
         * This should be defined as a final static object (constant).
         *
         * The class that contains the definition must be
         * registered with [Messages.registerMessages] prior to creating a [DefaultMessageProvider]
         * in order to have the default messages populate the language file.
         *
         * @param key The localization key for this message.
         * @param default The default message in whatever your plugin's primary language is.
         * @param additionalLines This param allows additional lines to be added to the message. This is optional and
         *                        multiline messages can be given in the original parameter by using line break characters.
         */
        @JvmStatic
        fun createMessage(key: String, default: String, vararg additionalLines: String): Message {
            return Message(key, (listOf(default) + additionalLines).joinToString("\n"))
        }

        /**
         * Creates a new message that will not be localized.
         *
         * This is intended to be used sparingly or to provide a simpler api for 3rd parties. These messages will not have
         * an entry in any language files.
         *
         * @param message The non localized message.
         * @param additionalLines This param allows additional lines to be added to the message. This is optional and
         *                        multiline messages can be given in the original parameter by using line break characters.
         */
        @JvmStatic
        fun createStaticMessage(message: String, vararg additionalLines: String): Message {
            return Message(null, (listOf(message) + additionalLines).joinToString("\n"))
        }

        /**
         * Bundles a [Message] with preset arguments.
         *
         * Can be used in cases where you are required to return a message of some sort and it is otherwise impossible to
         * return a localized message due to also requiring arguments.
         *
         * @param message The localization message for the bundle.
         * @param args The arguments for the bundled message.
         */
        @JvmStatic
        @Deprecated("This method now has a non-static method that is preferred.",
                ReplaceWith("message.bundle(*args)"))
        fun bundleMessage(message: Message, vararg args: Any): BundledMessage {
            return message.bundle(*args)
        }

        init {
            // Load the theme from theme.xml
            val dbFactory = DocumentBuilderFactory.newInstance()
            try {
                val urls = Messages::class.java.classLoader.getResources(Theme.getThemeResource())
                if (urls.hasMoreElements()) {
                    try {
                        val documentBuilder = dbFactory.newDocumentBuilder()
                        try {
                            Theme.loadTheme(documentBuilder.parse(urls.nextElement().openStream()))
                        } catch (e: SAXException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } catch (e: ParserConfigurationException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private val PATTERN = Pattern.compile("%s")

        @JvmStatic
        protected fun countArgs(def: String): Int {
            val matcher = PATTERN.matcher(def)
            var count = 0
            while (matcher.find()) {
                count++
            }
            return count
        }
    }
}
