/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.sponge.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.ChatColor;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.messages.Messages;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.Messager;

import java.util.LinkedList;
import java.util.List;

/**
 * A Sponge specific implementation of {@link pluginbase.messages.messaging.Messager}.
 * <p/>
 * Provides word wrapping on messages too long to fit on one line.
 * <p/>
 * Please refer to {@link pluginbase.messages.messaging.Messager} for javadoc for the methods in this class.  This class merely adds
 * convenience methods for Sponge CommandSenders.
 */
public class SpongeMessager extends Messager {

    /**
     * Creates a new messager backed by the given message provider.
     *
     * @param provider the backing message provider.
     */
    protected SpongeMessager(@NotNull final MessageProvider provider) {
        super(provider);
    }

    /** {@inheritDoc} */
    @Override
    public void message(@NotNull final MessageReceiver sender, @NotNull final String message) {
        sendMessages(sender, WordWrapper.wrapWords(message));
    }

    protected void send(@NotNull final CommandSource sender,
                        @Nullable final String prefix,
                        @NotNull final Message message,
                        @NotNull final Object... args) {
        String string = getLocalizedMessage(message, args);
        if (prefix != null && !prefix.isEmpty()) {
            string = prefix + " " + string;
        }
        message(sender, string);
    }

    public void message(@NotNull final CommandSource sender, @NotNull final String message) {
        sendMessages(sender, WordWrapper.wrapWords(message));
    }

    public void message(@NotNull final CommandSource sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, "", message, args);
    }

    public void message(@NotNull final CommandSource sender, @NotNull final BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    public void message(@NotNull final CommandSource player, @NotNull final List<String> messages) {
        for (String s : messages) {
            player.sendMessage(Text.of(s));
        }
    }

    public void messageSuccess(@NotNull final CommandSource sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message, args);
    }

    public void messageSuccess(@NotNull final CommandSource sender, @NotNull final BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.SUCCESS), message.getMessage(), message.getArgs());
    }

    public void messageSuccess(@NotNull final CommandSource sender, @NotNull final String message) {
        message(sender, getLocalizedMessage(Messages.SUCCESS) + " " + message);
    }

    public void messageError(@NotNull final CommandSource sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getLocalizedMessage(Messages.ERROR), message, args);
    }

    public void messageError(@NotNull final CommandSource sender, @NotNull final BundledMessage message) {
        send(sender, getLocalizedMessage(Messages.ERROR), message.getMessage(), message.getArgs());
    }

    public void messageError(@NotNull final CommandSource sender, @NotNull final String message) {
        message(sender, getLocalizedMessage(Messages.ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull final CommandSource player, @NotNull final String[] messages) {
        for (String message : messages) {
            player.sendMessage(Text.of(message));
        }
    }

    public void messageAndLog(@NotNull final CommandSource sender, @NotNull final Message message, @NotNull final Object... args) {
        if (!(sender instanceof ConsoleSource)) {
            message(sender, message, args);
        }
        getLog().info(getLocalizedMessage(message, args));
    }

    private static class WordWrapper {

        static final int CHAT_WIDTH = 55;

        static String[] wrapWords(@NotNull String rawString) {
            // A string shorter than the lineWidth is a single line
            if (rawString.length() <= CHAT_WIDTH && !rawString.contains("\n")) {
                return new String[] {rawString};
            }

            char[] rawChars = rawString.toCharArray();
            StringBuilder word = new StringBuilder();
            StringBuilder line = new StringBuilder();
            List<String> lines = new LinkedList<String>();
            int lineColorChars = 0;

            for (int i = 0; i < rawChars.length; i++) {
                char c = rawChars[i];

                // skip chat color modifiers
                if (c == ChatColor.COLOR_CHAR) {
                    word.append(ChatColor.getByChar(rawChars[i + 1]));
                    lineColorChars += 2;
                    i++; // Eat the next character as we have already processed it
                    continue;
                }

                if (c == ' ' || c == '\n') {
                    if (line.length() == 0 && word.length() > CHAT_WIDTH) { // special case: extremely long word begins a line
                        for (String partialWord : word.toString().split("(?<=\\G.{" + CHAT_WIDTH + "})")) {
                            lines.add(partialWord);
                        }
                    } else if (line.length() + word.length() - lineColorChars == CHAT_WIDTH) { // Line exactly the correct length...newline
                        line.append(word);
                        lines.add(line.toString());
                        line = new StringBuilder();
                        lineColorChars = 0;
                    } else if (line.length() + 1 + word.length() - lineColorChars > CHAT_WIDTH) { // Line too long...break the line
                        for (String partialWord : word.toString().split("(?<=\\G.{" + CHAT_WIDTH + "})")) {
                            lines.add(line.toString());
                            line = new StringBuilder(partialWord);
                        }
                        lineColorChars = 0;
                    } else {
                        if (line.length() > 0) {
                            line.append(' ');
                        }
                        line.append(word);
                    }
                    word = new StringBuilder();

                    if (c == '\n') { // Newline forces the line to flush
                        lines.add(line.toString());
                        line = new StringBuilder();
                    }
                } else {
                    word.append(c);
                }
            }

            if(line.length() > 0) { // Only add the last line if there is anything to add
                lines.add(line.toString());
            }

            // Iterate over the wrapped lines, applying the last color from one line to the beginning of the next
            if (lines.get(0).length() == 0 || lines.get(0).charAt(0) != ChatColor.COLOR_CHAR) {
                lines.set(0, ChatColor.WHITE + lines.get(0));
            }
            for (int i = 1; i < lines.size(); i++) {
                final String pLine = lines.get(i-1);
                final String subLine = lines.get(i);

                char color = pLine.charAt(pLine.lastIndexOf(ChatColor.COLOR_CHAR) + 1);
                if (subLine.length() == 0 || subLine.charAt(0) != ChatColor.COLOR_CHAR) {
                    lines.set(i, ChatColor.getByChar(color) + subLine);
                }
            }

            return lines.toArray(new String[lines.size()]);
        }
    }
}

