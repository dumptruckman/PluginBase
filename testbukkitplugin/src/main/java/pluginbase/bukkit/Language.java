package pluginbase.bukkit;

import pluginbase.messages.Message;

public class Language {
    static final Message TEST_MESSAGE = Message.createMessage("test.message", "This is a test!");

    static class Nest {
        static final Message NESTED_TEST_MESSAGE = Message.createMessage("test.nested.message", "Nested message.");
    }
}
