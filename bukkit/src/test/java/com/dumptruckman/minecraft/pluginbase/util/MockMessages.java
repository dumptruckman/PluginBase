package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.messages.Message;

public class MockMessages {
    public final static Message TEST_MESSAGE = Message.createMessage("test.message", "This is a &ctest message! (%1)"
            + "\nAnd an additional line.");

    public static void init() { }
}
