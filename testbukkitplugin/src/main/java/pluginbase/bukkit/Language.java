package pluginbase.bukkit;

import pluginbase.messages.Message;

public class Language {
    static Message TEST_MESSAGE = Message.createMessage("test.message", "This is a test!");

    static class Nest {
        static Message NESTED_TEST_MESSAGE = Message.createMessage("test.nested.message", "Nested message.");
    }
}
