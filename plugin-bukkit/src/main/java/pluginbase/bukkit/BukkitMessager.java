/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit;

import pluginbase.messages.BundledMessage;
import pluginbase.messages.Localizable;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.messages.Messages;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.Messager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * A Bukkit specific implementation of {@link pluginbase.messages.messaging.Messager}.
 * <p/>
 * Provides word wrapping on messages too long to fit on one line.
 * <p/>
 * Please refer to {@link pluginbase.messages.messaging.Messager} for javadoc for the methods in this class.  This class merely adds
 * convenience methods for Bukkit CommandSenders.
 */
public class BukkitMessager extends Messager {

    /**
     * Creates a new messager backed by the given message provider.
     *
     * @param provider the backing message provider.
     */
    protected BukkitMessager(@NotNull final MessageProvider provider) {
        super(provider);
    }

    /**
     * Loads the given language file into a new BukkitMessager set to use the given locale.
     * <p/>
     * Any messages registered with {@link Messages#registerMessages(Localizable, Class)} for the same Localizable object
     * should be present in this file.  If they are not previously present, they will be inserted with the default
     * message.  If any message is located in the file that is not registered as previously mentioned it will be
     * removed from the file.
     *
     * @param localizable the object that registered localizable messages.
     * @param languageFile the language file to load localized messages from.
     * @param locale the locale to use when formatting the messages.
     * @return a new messager loaded with the messages from the given language file and locale.
     */
    public static BukkitMessager loadMessagerWithMessages(@NotNull final Localizable localizable,
                                                          @NotNull final File languageFile,
                                                          @NotNull final Locale locale) {
        return new BukkitMessager(Messages.loadMessages(localizable, languageFile, locale));
    }

    /** {@inheritDoc} */
    @Override
    public void message(@NotNull final MessageReceiver sender, @NotNull final String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }

    protected void send(@NotNull final CommandSender sender,
                        @Nullable final String prefix,
                        @NotNull final Message message,
                        @NotNull final Object... args) {
        String string = getLocalizedMessage(message, args);
        if (prefix != null && !prefix.isEmpty()) {
            string = prefix + " " + string;
        }
        message(sender, string);
    }

    public void message(@NotNull final CommandSender sender, @NotNull final String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }

    public void message(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, "", message, args);
    }

    public void message(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    public void message(@NotNull final CommandSender player, @NotNull final List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message, args);
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message.getMessage(), message.getArgs());
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final String message) {
        message(sender, getLocalizedMessage(Messages.SUCCESS) + " " + message);
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getLocalizedMessage(Messages.ERROR), message, args);
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.ERROR), message.getMessage(), message.getArgs());
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final String message) {
        message(sender, getLocalizedMessage(Messages.ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull final CommandSender player, @NotNull final String[] messages) {
        player.sendMessage(messages);
    }

    public void messageAndLog(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            message(sender, message, args);
        }
        getLog().info(getLocalizedMessage(message, args));
    }
}

