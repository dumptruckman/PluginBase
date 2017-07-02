/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import org.xml.sax.SAXException
import pluginbase.logging.Logging
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * A localization key and its defaults.
 *
 * The key represents the location of the localized strings in a language file.
 *
 * The default is what should populate the localization file by default.
 */
internal class DefaultMessage internal constructor (key: String?, default: String) : Message {

    /**
     * The default message in whatever your plugin's primary language is.
     *
     * @return The default non-localized messages.
     */
    override val default: String = Theme.parseMessage(default)

    /**
     * The localization key for the message. Each object in the array represents a configuration node path.
     *
     * @return The localization key for the message.
     */
    override val key: Array<Any>? = key?.split(".")?.dropLastWhile { it.isEmpty() }?.toTypedArray()

    /**
     * Gets the number of expected arguments for this message.
     *
     * This is used to validate localized versions of this message to ensure they were given the appropriate
     * amount of arguments.
     *
     * @return the number of expected arguments for this message.
     */
    override val argCount: Int = Messages.countArgs(this.default)

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
    override fun bundle(vararg args: Any?): BundledMessage {
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

        init {
            // Load the theme from theme.xml
            val dbFactory = DocumentBuilderFactory.newInstance()
            try {
                val urls = Messages::class.java.classLoader.getResources(Theme.themeResource)
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
    }
}