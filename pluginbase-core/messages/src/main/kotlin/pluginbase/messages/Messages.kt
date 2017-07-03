/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import ninja.leaping.configurate.loader.ConfigurationLoader
import pluginbase.logging.Logging

import java.lang.reflect.Modifier
import java.util.Locale
import java.util.regex.Pattern

/**
 * A class used for registering and keeping track of localized messages.
 */
object Messages {

    private val PATTERN = Pattern.compile("%s")

    private val messages = mutableMapOf<LocalizablePlugin, MutableMap<Array<*>?, Message>>()

    private fun getMessagesForPlugin(localizablePlugin: LocalizablePlugin): MutableMap<Array<*>?, Message> {
        return messages[localizablePlugin] ?: throw IllegalArgumentException("Provider has no registered messages.")
    }

    @JvmStatic
    internal fun getMessageKeys(localizablePlugin: LocalizablePlugin): MutableSet<Array<*>?> {
        return getMessagesForPlugin(localizablePlugin).keys
    }

    @JvmStatic
    internal fun getMessage(localizablePlugin: LocalizablePlugin, key: Array<*>?): Message? {
        val messagesForPlugin = getMessagesForPlugin(localizablePlugin)
        if (key == null) {
            return BLANK
        }
        return messagesForPlugin[key]
    }

    @JvmStatic
    internal fun containsMessageKey(localizablePlugin: LocalizablePlugin, key: Array<*>): Boolean {
        val messagesForPlugin = getMessagesForPlugin(localizablePlugin)
        return messagesForPlugin.containsKey(key)
    }

    /** A message with no text.  */
    @JvmField
    val BLANK = createStaticMessage("")

    /** Used for wrapping regular exceptions into a PluginBaseException.  */
    @JvmField
    val EXCEPTION = createMessage("generic.exception", Theme.PLAIN.toString() + "%s")

    /** Used for wrapping PluginBaseExceptions into a PluginBaseException.  */
    @JvmField
    val CAUSE_EXCEPTION = createMessage("generic.cause_exception", Theme.PLAIN.toString() + "Caused by: %s")

    /** A message of general success  */
    @JvmField
    val SUCCESS = createMessage("generic.success", Theme.SUCCESS.toString() + "[SUCCESS]")

    /** A message of general error  */
    @JvmField
    val ERROR = createMessage("generic.error", Theme.ERROR.toString() + "[ERROR]")

    /** A message of general loading error  */
    @JvmField
    val COULD_NOT_LOAD = createMessage("generic.could_not_load", "Could not load: %s")
    @JvmField
    /** A message of general loading error  */
    val COULD_NOT_SAVE = createMessage("generic.could_not_save", "Could not save: %s")

    /**
     * Registers all of the messages in a given class and all inner classes to the localizablePlugin object.
     *
     * Messages are defined with the [Message] class and should be declared as static and final (constant).
     * Their access modifier is not important and should be set as your needs dictate.
     *
     * This method will import all of the Messages defined per the above guide lines that exist in the class.
     * Those messages imported will be linked to the given localizablePlugin object.
     *
     * @param localizablePlugin the "owner" of the message.  Typically this a plugin who will be using the messages.
     * @param clazz the class that contains the definition of the messages.
     */
    @JvmStatic
    fun registerMessages(localizablePlugin: LocalizablePlugin, clazz: Class<*>) {
        Logging.finer("Registering messages in class %s to %s", clazz, localizablePlugin)
        if (clazz == Messages::class.java) {
            return
        }
        if (!messages.containsKey(localizablePlugin)) {
            messages.put(localizablePlugin,  mutableMapOf<Array<*>?, Message>())
        }

        val messages = getMessagesForPlugin(localizablePlugin)

        messages.putIfAbsent(SUCCESS.key, SUCCESS)
        messages.putIfAbsent(ERROR.key, ERROR)
        messages.putIfAbsent(EXCEPTION.key, EXCEPTION)
        messages.putIfAbsent(CAUSE_EXCEPTION.key, CAUSE_EXCEPTION)

        val fields = clazz.declaredFields + clazz.fields
        for (field in fields) {
            if (Modifier.isStatic(field.modifiers)
                    && Modifier.isFinal(field.modifiers)
                    && Message::class.java.isAssignableFrom(field.type)) {
                val access = field.isAccessible
                if (!access) {
                    field.isAccessible = true
                }
                try {
                    val message = field.get(null) as Message ?: continue
                    val key = message.key ?: continue
                    if (messages.putIfAbsent(key, message) == null) {
                        Logging.finest("Registering message with key %s", key.joinToString("."))
                    }
                } catch (e: IllegalAccessException) {
                    Logging.warning("Could not register language message '%s' from class '%s'", field, clazz)
                }

                if (!access) {
                    field.isAccessible = false
                }
            }
        }

        for (c in clazz.declaredClasses) {
            registerMessages(localizablePlugin, c)
        }
    }

    /**
     * Loads the given language file into a new MessageProvider set to use the given locale.
     *
     * Any messages registered with [.registerMessages] for the same LocalizablePlugin object
     * should be present in this file.  If they are not previously present, they will be inserted with the default
     * message.  If any message is located in the file that is not registered as previously mentioned it will be
     * removed from the file.
     *
     * @param localizablePlugin the object that registered localizable messages.
     * @param loader the config loader which will load the messages.
     * @param locale the locale to use when formatting the messages.
     * @return a new MessagerProvider loaded with the messages from the given language file and locale.
     */
    @JvmStatic
    fun loadMessages(localizablePlugin: LocalizablePlugin,
                     loader: ConfigurationLoader<*>,
                     locale: Locale): MessageProvider {
        return DefaultMessageProvider(localizablePlugin, loader, locale)
    }

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
        return DefaultMessage(key, (listOf(default) + additionalLines).joinToString("\n"))
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
        return DefaultMessage(null, (listOf(message) + additionalLines).joinToString("\n"))
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
    fun bundleMessage(message: Message, vararg args: Any?): BundledMessage {
        return message.bundle(*args)
    }

    @JvmStatic
    internal fun countArgs(def: String): Int {
        val matcher = PATTERN.matcher(def)
        var count = 0
        while (matcher.find()) {
            count++
        }
        return count
    }
}

