package pluginbase.bukkit;

import pluginbase.messages.Message;

public class Language {
    public static final Message TEST_MESSAGE = Message.createMessage("test.message", "This is a test!");

    public static class Nest {
        public static final Message NESTED_TEST_MESSAGE = Message.createMessage("test.nested.message", "Nested message.");
    }
}
