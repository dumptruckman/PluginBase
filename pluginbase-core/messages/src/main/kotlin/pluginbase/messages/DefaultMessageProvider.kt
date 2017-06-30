/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import pluginbase.logging.PluginLogger

import java.io.IOException
import java.util.Arrays
import java.util.IllegalFormatException
import java.util.Locale

internal class DefaultMessageProvider(override val plugin: LocalizablePlugin,
                                      loader: ConfigurationLoader<*>,
                                      private val locale: Locale) : MessageProvider {

    private val messages: ConfigurationNode

    init {
        messages = load(plugin, loader)
        prune(plugin, messages)
        save(loader, messages)
    }

    private fun save(loader: ConfigurationLoader<*>, language: ConfigurationNode) {
        try {
            loader.save(language)
        } catch (e: IOException) {
            log.warning("Problem saving current language file")
            e.printStackTrace()
        }

    }

    private fun prune(localizable: LocalizablePlugin, language: ConfigurationNode) {
        // Prune file
        val it = language.childrenList.iterator()
        while (it.hasNext()) {
            val key = it.next().path
            if (!Messages.containsMessageKey(localizable, key)) {
                log.finer("Removing unused language: %s", *key)
                it.remove()
            }
        }
    }

    private fun load(localizable: LocalizablePlugin, loader: ConfigurationLoader<*>): ConfigurationNode {
        var language: ConfigurationNode
        try {
            language = loader.load()
        } catch (e: IOException) {
            e.printStackTrace()
            language = loader.createEmptyNode()
        }

        // TODO check if languages is empty?

        for (key in Messages.getMessageKeys(localizable)) {
            val message = Messages.getMessage(localizable, key)
            val node = language.getNode(*key)
            if (message != null && node.isVirtual) {
                if (node.isVirtual) {
                    node.value = message.default
                    log.finest("Created new message in language file: %s", message)
                } else if (Message.countArgs(node.string) != message.argCount) {
                    node.value = message.default
                    log.warning("The message for '%s' in the current language file does not have the correct amount of arguments (%s).  The default will be used.", key, message.argCount)
                }
            }
        }
        return language
    }

    private fun _getMessage(key: Array<Any>): String {
        val node = this.messages.getNode(*key)
        if (node.isVirtual) {
            val keyString = Arrays.toString(key)
            log.warning("There is not language entry for %s.  Was it registered?", keyString)
            return keyString
        }
        return node.string
    }

    override fun getLocalizedMessage(key: Message, vararg args: Any?): String {
        if (key.key == null) {
            return formatMessage(null, key.default, *args)
        }
        return getLocalizedMessage(key.key, *args)
    }

    override fun getLocalizedMessage(key: Array<Any>, vararg args: Any?): String {
        val message = _getMessage(key)
        return formatMessage(key, message, *args)
    }

    private fun formatMessage(key: Array<Any>?, message: String, vararg args: Any?): String {
        try {
            return formatMessage(locale, message, *args)
        } catch (e: IllegalFormatException) {
            log.warning("Language string format is incorrect: %s: %s", key, message)
            // Help identify where the incorrect language elements are
            for (ste in Thread.currentThread().stackTrace) {
                log.warning(ste.toString())
            }
            return message
        }

    }

    val log: PluginLogger
        get() = plugin.log
}
