package pluginbase.util;

import pluginbase.messages.Message;

public class MockMessages {
    public final static Message TEST_MESSAGE = Message.createMessage("test.message", "This is a &ctest message! (%s)"
            + "\nAnd an additional line.");

    public static void init() { }
}
