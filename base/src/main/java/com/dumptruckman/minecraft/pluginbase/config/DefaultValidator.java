package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;

class DefaultValidator implements EntryValidator {

    public boolean isValid(Object obj) {
        return true;
    }

    @Override
    public Message getInvalidMessage() {
        return Messages.BLANK;
    }
}
