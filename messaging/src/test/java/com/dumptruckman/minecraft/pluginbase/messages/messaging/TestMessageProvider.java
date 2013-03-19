package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.logging.PluginLogger;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.MessageProvider;
import org.jetbrains.annotations.NotNull;

public class TestMessageProvider implements MessageProvider {

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args) {
        return String.format(key.getDefault(), args);
    }

    @NotNull
    @Override
    public PluginLogger getLog() {
        return Logging.getLogger();
    }
}
