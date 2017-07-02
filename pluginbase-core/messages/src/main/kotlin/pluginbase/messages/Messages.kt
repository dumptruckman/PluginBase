/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import ninja.leaping.configurate.loader.ConfigurationLoader
import pluginbase.logging.Logging

import java.lang.reflect.Modifier
import java.util.Locale

/**
 * A class used for registering and keeping track of localized messages.
 */
object Messages {

    private val messages = mutableMapOf<LocalizablePlugin, MutableMap<Array<Any>?, Message>>()

    private fun getMessagesForPlugin(localizablePlugin: LocalizablePlugin): MutableMap<Array<Any>?, Message> {
        return messages[localizablePlugin] ?: throw IllegalArgumentException("Provider has no registered messages.")
    }

    @JvmStatic
    internal fun getMessageKeys(localizablePlugin: LocalizablePlugin): MutableSet<Array<Any>?> {
        return getMessagesForPlugin(localizablePlugin).keys
    }

    @JvmStatic
    internal fun getMessage(localizablePlugin: LocalizablePlugin, key: Array<Any>?): Message? {
        val messagesForPlugin = getMessagesForPlugin(localizablePlugin)
        println(key)
        println(messagesForPlugin.keys)
        if (key == null) {
            return BLANK
        }
        return messagesForPlugin[key]
    }

    @JvmStatic
    internal fun containsMessageKey(localizablePlugin: LocalizablePlugin, key: Array<Any>): Boolean {
        val messagesForPlugin = getMessagesForPlugin(localizablePlugin)
        return messagesForPlugin.containsKey(key)
    }

    /** A message with no text.  */
    @JvmField
    val BLANK = Message.createStaticMessage("")

    /** Used for wrapping regular exceptions into a PluginBaseException.  */
    @JvmField
    val EXCEPTION = Message.createMessage("generic.exception", Theme.PLAIN.toString() + "%s")

    /** Used for wrapping PluginBaseExceptions into a PluginBaseException.  */
    @JvmField
    val CAUSE_EXCEPTION = Message.createMessage("generic.cause_exception", Theme.PLAIN.toString() + "Caused by: %s")

    /** A message of general success  */
    @JvmField
    val SUCCESS = Message.createMessage("generic.success", Theme.SUCCESS.toString() + "[SUCCESS]")

    /** A message of general error  */
    @JvmField
    val ERROR = Message.createMessage("generic.error", Theme.ERROR.toString() + "[ERROR]")

    /** A message of general loading error  */
    @JvmField
    val COULD_NOT_LOAD = Message.createMessage("generic.could_not_load", "Could not load: %s")
    @JvmField
    /** A message of general loading error  */
    val COULD_NOT_SAVE = Message.createMessage("generic.could_not_save", "Could not save: %s")

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
            messages.put(localizablePlugin,  mutableMapOf<Array<Any>?, Message>())
        }

        val messages = getMessagesForPlugin(localizablePlugin)

        messages.putIfAbsent(SUCCESS.key, SUCCESS)
        messages.putIfAbsent(ERROR.key, ERROR)
        messages.putIfAbsent(EXCEPTION.key, EXCEPTION)
        messages.putIfAbsent(CAUSE_EXCEPTION.key, CAUSE_EXCEPTION)

        val fields = clazz.declaredFields + clazz.fields
        for (field in fields) {
            println(field)
            if (Modifier.isStatic(field.modifiers)
                    && Modifier.isFinal(field.modifiers)
                    && Message::class.java.isAssignableFrom(field.type)) {
                val access = field.isAccessible
                if (!access) {
                    field.isAccessible = true
                }
                try {
                    val message = field.get(null) as Message ?: continue
                    println("Registering " + message.key.toString())
                    message.key ?: messages.putIfAbsent(message.key, message)
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
}

