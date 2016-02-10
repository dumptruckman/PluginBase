package pluginbase.testplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;

public enum Language implements Message {
    TEST_MESSAGE("test.message", "This is a test!")
    ;

    private final Message message;

    Language(@NotNull String key, @NotNull String defaultMessage, String... additionalLines) {
        this.message = Message.createMessage(key, defaultMessage, additionalLines);
    }

    @Override
    @NotNull
    public String getDefault() {
        return message.getDefault();
    }

    @Override
    @Nullable
    public Object[] getKey() {
        return message.getKey();
    }

    @Override
    public int getArgCount() {
        return message.getArgCount();
    }

    public static class Nest {
        public static final Message NESTED_TEST_MESSAGE = Message.createMessage("test.nested.message", "Nested message.");
    }
}
