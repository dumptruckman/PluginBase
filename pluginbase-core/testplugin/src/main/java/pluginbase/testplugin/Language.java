package pluginbase.testplugin;

import pluginbase.messages.Message;
import pluginbase.messages.Messages;

public class Language {

    public static final Message TEST_MESSAGE = Messages.createMessage("test.message", "This is a test!");

    public static class Nest {
        public static final Message NESTED_TEST_MESSAGE = Messages.createMessage("test.nested.message", "Nested message.");
    }
}
