package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

public interface EntryValidator {

    boolean isValid(Object obj);

    Message getInvalidMessage();
}
