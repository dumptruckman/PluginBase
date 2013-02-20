package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class SimpleMessager extends SimpleMessageProvider implements Messager {

    public SimpleMessager(@NotNull File dataFolder) {
        super(dataFolder);
    }

    protected void send(@NotNull final MessageReceiver sender,
                        @Nullable final String prefix,
                        @NotNull final Message message,
                        @NotNull final Object... args) {
        String string = getMessage(message, args);
        if (prefix != null && !prefix.isEmpty()) {
            string = prefix + " " + string;
        }
        message(sender, string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(@NotNull MessageReceiver sender, @NotNull Message message, Object... args) {
        send(sender, "", message, args);
    }

    @Override
    public void message(@NotNull MessageReceiver sender, @NotNull BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(@NotNull MessageReceiver sender, @NotNull String message) {
        sender.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(@NotNull MessageReceiver player, @NotNull List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    @Override
    public void messageSuccess(MessageReceiver sender, Message message, Object... args) {
        send(sender, getMessage(SUCCESS), message, args);
    }

    @Override
    public void messageSuccess(MessageReceiver sender, BundledMessage message) {
        send(sender, getMessage(SUCCESS), message.getMessage(), message.getArgs());
    }

    @Override
    public void messageSuccess(MessageReceiver sender, String message) {
        message(sender, getMessage(SUCCESS) + " " + message);
    }

    @Override
    public void messageError(MessageReceiver sender, Message message, Object... args) {
        send(sender, getMessage(ERROR), message, args);
    }

    @Override
    public void messageError(MessageReceiver sender, BundledMessage message) {
        send(sender, getMessage(ERROR), message.getMessage(), message.getArgs());
    }

    @Override
    public void messageError(MessageReceiver sender, String message) {
        message(sender, getMessage(ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull MessageReceiver player, @NotNull String[] messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    @Override
    public void messageAndLog(@NotNull MessageReceiver sender, @NotNull Message message, @NotNull Object... args) {
        if (sender.isPlayer()) {
            message(sender, message, args);
        }
        Logging.info(getMessage(message, args));
    }
}
