/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages.messaging;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.LocalizablePlugin;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.messages.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * This interface describes a Messager which sends localized messages to {@link MessageReceiver}s.
 */
public class Messager implements MessageProvider {

    public static final String DEBUG_BROADCAST_PREFIX = "[Message sent to %s] ";

    @Nullable
    private static WeakReference<Messager> staticMessager = null;

    /**
     * Retrieves the most recently created Messager object.
     * <p/>
     * You should not store any hard references to the returned Messager as it needs to be able to be garbage collected
     * as needed.
     *
     * @return the most recently created messager object or null if none created or all have been garbage collected.
     */
    @Nullable
    public static Messager requestMessager() {
        return staticMessager != null ? staticMessager.get() : null;
    }

    @NotNull
    private final MessageProvider provider;
    @NotNull
    final Map<MessageReceiver, MessagerDebugBroadcast> debugBroadcastMap = new HashMap<>();

    /**
     * Creates a new messager backed by the given message provider.
     *
     * @param provider the backing message provider.
     */
    protected Messager(@NotNull final MessageProvider provider) {
        this.provider = provider;
        staticMessager = new WeakReference<Messager>(this);
    }

    /**
     * Loads the given language file into a new Messager set to use the given locale.
     * <p/>
     * Any messages registered with {@link Messages#registerMessages(pluginbase.messages.LocalizablePlugin, Class)} for the same Localizable object
     * should be present in this file.  If they are not previously present, they will be inserted with the default
     * message.  If any message is located in the file that is not registered as previously mentioned it will be
     * removed from the file.
     *
     * @param localizablePlugin the object that registered localizable messages.
     * @param loader the config file loader to load localized messages with.
     * @param locale the locale to use when formatting the messages.
     * @return a new messager loaded with the messages from the given language file and locale.
     */
    public static Messager loadMessagerWithMessages(@NotNull final LocalizablePlugin localizablePlugin,
                                                    @NotNull final ConfigurationLoader loader,
                                                    @NotNull final Locale locale) {
        return new Messager(Messages.loadMessages(localizablePlugin, loader, locale));
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args) {
        return provider.getLocalizedMessage(key, args);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final Object[] key, @NotNull final Object... args) {
        return provider.getLocalizedMessage(key, args);
    }

    protected void send(@NotNull final MessageReceiver sender,
                        @Nullable final String prefix,
                        @NotNull final Message message,
                        @NotNull final Object... args) {
        String string = getLocalizedMessage(message, args);
        if (prefix != null && !prefix.isEmpty()) {
            string = prefix + " " + string;
        }
        message(sender, string);
    }

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    public void message(@NotNull MessageReceiver sender, @NotNull Message message, Object... args) {
        send(sender, "", message, args);
    }

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    public void message(@NotNull MessageReceiver sender, @NotNull BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param sender  Player to send message to.
     * @param message Message to send.
     */
    public void message(@NotNull MessageReceiver sender, @NotNull String message) {
        sender.sendMessage(message);
        if (debugBroadcastMap.containsKey(sender)) {
            MessagerDebugBroadcast broadcast = debugBroadcastMap.get(sender);
            broadcast.messageRecord(String.format(DEBUG_BROADCAST_PREFIX, sender) + message);
        }
    }

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param sender   Player to send message to.
     * @param messages Messages to send.
     */
    public void message(@NotNull MessageReceiver sender, @NotNull List<String> messages) {
        for (String s : messages) {
            sender.sendMessage(s);
            if (debugBroadcastMap.containsKey(sender)) {
                MessagerDebugBroadcast broadcast = debugBroadcastMap.get(sender);
                broadcast.messageRecord(String.format(DEBUG_BROADCAST_PREFIX, sender) + s);
            }
        }
    }

    /**
     * Sends a message to the specified player with a prefix indicating success.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    public void messageSuccess(MessageReceiver sender, Message message, Object... args) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message, args);
    }

    /**
     * Sends a message to the specified player with a prefix indicating success.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    public void messageSuccess(MessageReceiver sender, BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message.getMessage(), message.getArgs());
    }

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     * This message will have a prefix indicating success.
     *
     * @param sender  Player to send message to.
     * @param message Message to send.
     */
    public void messageSuccess(MessageReceiver sender, String message) {
        message(sender, getLocalizedMessage(Messages.SUCCESS) + " " + message);
    }

    /**
     * Sends a message to the specified player with a prefix indicating failure.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    public void messageError(MessageReceiver sender, Message message, Object... args) {
        send(sender, getLocalizedMessage(Messages.ERROR), message, args);
    }

    /**
     * Sends a message to the specified player with a prefix indicating failure.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    public void messageError(MessageReceiver sender, BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.ERROR), message.getMessage(), message.getArgs());
    }

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     * This message will have a prefix indicating failure.
     *
     * @param sender  Player to send message to.
     * @param message Message to send.
     */
    public void messageError(MessageReceiver sender, String message) {
        message(sender, getLocalizedMessage(Messages.ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull MessageReceiver player, @NotNull String[] messages) {
        for (String s : messages) {
            player.sendMessage(s);
            if (debugBroadcastMap.containsKey(player)) {
                MessagerDebugBroadcast broadcast = debugBroadcastMap.get(player);
                broadcast.messageRecord(String.format(DEBUG_BROADCAST_PREFIX, player) + s);
            }
        }
    }

    /**
     * Sends a message to the sender and logs it in the server logs.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    public void messageAndLog(@NotNull MessageReceiver sender, @NotNull Message message, @NotNull Object... args) {
        if (sender.isPlayer()) {
            message(sender, message, args);
        }
        getLog().info(getLocalizedMessage(message, args));
    }

    @NotNull
    @Override
    public LocalizablePlugin getPlugin() {
        return provider.getPlugin();
    }

    @NotNull
    protected PluginLogger getLog() {
        return getPlugin().getLog();
    }

    /**
     * Returns the debug broadcaster for the given MessageReceiver if it exists.
     *
     * @param messageReceiver the MessageReceiver to get the broadcaster for
     * @return the debug broadcaster for the given MessageReceiver or null if it does not exist.
     */
    @Nullable
    MessagerDebugBroadcast getDebugBroadcast(@NotNull MessageReceiver messageReceiver) {
        return debugBroadcastMap.get(messageReceiver);
    }

    public boolean subscribeToDebugBroadcast(@NotNull MessagerDebugSubscription subscription) {
        MessagerDebugBroadcast broadcast = getDebugBroadcast(subscription.getSubscriber());
        if (broadcast == null) {
            broadcast = new MessagerDebugBroadcast();
            debugBroadcastMap.put(subscription.getSubscriber(), broadcast);
        }
        return broadcast.acceptSubscription(subscription);
    }

    public boolean unsubscribeFromDebugBroadcast(@NotNull MessagerDebugSubscription subscription) {
        MessagerDebugBroadcast broadcast = getDebugBroadcast(subscription.getSubscriber());
        return broadcast != null && broadcast.cancelSubscription(subscription);
    }
}

